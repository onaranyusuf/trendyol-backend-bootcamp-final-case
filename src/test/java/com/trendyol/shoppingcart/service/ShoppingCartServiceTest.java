package com.trendyol.shoppingcart.service;

import com.trendyol.shoppingcart.exception.ShoppingCartException;
import com.trendyol.shoppingcart.models.dto.ItemDTO;
import com.trendyol.shoppingcart.models.dto.VasItemDTO;
import com.trendyol.shoppingcart.models.dto.ResponseDTO;
import com.trendyol.shoppingcart.models.entity.Item;
import com.trendyol.shoppingcart.repository.ItemRepository;
import com.trendyol.shoppingcart.mapper.ItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ShoppingCartServiceTest {

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private VasItemService vasItemService;

    @Mock
    private PromotionService promotionService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(shoppingCartService, "maxTotalItemCount", 30);
        ReflectionTestUtils.setField(shoppingCartService, "maxTotalCartAmount", 500000);

    }


    @Test
    public void givenValidItem_whenAddItemToShoppingCart_thenItemIsAdded() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(1234);
        itemDTO.setCategoryId(1212);
        itemDTO.setPrice(20.0);
        itemDTO.setQuantity(6);

        Item item = new Item(5, 1234, 1212, 20.0, 6, null);
        when(itemRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(item);

        // When
        ResponseDTO<String> responseDTO = shoppingCartService.addItemToShoppingCart(itemDTO);

        // Then
        assertTrue(responseDTO.isResult());
        assertEquals("Item has been added to the shopping cart successfully", responseDTO.getMessage());
    }

    @Test
    public void givenValidVasItem_whenAddVasItemToShoppingCart_thenVasItemIsAdded() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setItemId(1234);
        vasItemDTO.setCategoryId(1212);
        vasItemDTO.setPrice(20.0);
        vasItemDTO.setQuantity(2);

        Item item = new Item(5, 1234, 1212, 20.0, 6, null);
        when(itemRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(item);

        // When
        ResponseDTO<String> responseDTO = shoppingCartService.addVasItemToShoppingCart(vasItemDTO);

        // Then
        assertTrue(responseDTO.isResult());
        assertEquals("Vas Item has been added to shopping cart successfully", responseDTO.getMessage());
    }



    @Test
    public void givenValidItem_whenCalculateTotalPrice_thenTotalPriceIsCalculated() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(1234);
        itemDTO.setCategoryId(1212);
        itemDTO.setPrice(20.0);
        itemDTO.setQuantity(6);

        Item item = new Item(5, 1234, 1212, 20.0, 6, null);
        when(itemRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(item);

        // When
        ResponseDTO<String> responseDTO = shoppingCartService.addItemToShoppingCart(itemDTO);

        // Then
        assertTrue(responseDTO.isResult());
        assertEquals("Item has been added to the shopping cart successfully", responseDTO.getMessage());
    }

    @Test
    public void givenValidShoppingCart_whenResetShoppingCart_thenShoppingCartIsReset() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setPrice(10.0);
        itemDTO.setQuantity(3);

        // Add an item to the shopping cart
        ResponseDTO<String> addItemResponse = shoppingCartService.addItemToShoppingCart(itemDTO);
        assertTrue(addItemResponse.isResult());

        // When
        ResponseDTO<String> resetResponse = shoppingCartService.resetShoppingCart();

        // Then
        assertTrue(resetResponse.isResult());
        assertEquals("Shopping cart has been reset successfully", resetResponse.getMessage());
        verify(itemService, times(1)).resetItems();

    }

    @Test
    public void givenValidShoppingCart_whenRemoveItemFromShoppingCart_thenItemIsRemoved() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setPrice(10.0);
        itemDTO.setQuantity(3);

        // Add an item to the shopping cart
        ResponseDTO<String> addItemResponse = shoppingCartService.addItemToShoppingCart(itemDTO);
        assertTrue(addItemResponse.isResult());

        // When
        ResponseDTO<String> removeItemResponse = shoppingCartService.removeItemFromShoppingCart(itemDTO);

        // Then
        assertTrue(removeItemResponse.isResult());
        assertEquals("Item has been removed from shopping cart successfully", removeItemResponse.getMessage());
        verify(itemService, times(1)).removeItem(itemDTO);
    }

    @Test
    public void givenValidShoppingCart_whenRemoveVasItemFromShoppingCart_thenVasItemIsRemoved() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(3);

        // Add a vas item to the shopping cart
        ResponseDTO<String> addVasItemResponse = shoppingCartService.addVasItemToShoppingCart(vasItemDTO);
        assertTrue(addVasItemResponse.isResult());

        // When
        ResponseDTO<String> removeVasItemResponse = shoppingCartService.removeVasItemFromShoppingCart(vasItemDTO);

        // Then
        assertTrue(removeVasItemResponse.isResult());
        assertEquals("Vas Item has been removed from shopping cart successfully", removeVasItemResponse.getMessage());
        verify(vasItemService, times(1)).removeVasItem(vasItemDTO);

    }

    @Test
    public void givenValidItemWithCartMaxQuantity_whenAddItemToShoppingCart_thenShoppingCartExceptionThrown() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(1234);
        itemDTO.setCategoryId(1212);
        itemDTO.setPrice(20.0);
        itemDTO.setQuantity(31);

        when(itemRepository.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(ShoppingCartException.class, () -> shoppingCartService.addItemToShoppingCart(itemDTO));
    }

    @Test
    public void givenValidItemWithCartMaxTotalPrice_whenAddItemToShoppingCart_thenShoppingCartExceptionThrown() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(1234);
        itemDTO.setCategoryId(1212);
        itemDTO.setPrice(501000.0);
        itemDTO.setQuantity(2);

        when(itemRepository.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(ShoppingCartException.class, () -> shoppingCartService.addItemToShoppingCart(itemDTO));
    }

}
