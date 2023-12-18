package com.trendyol.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import com.trendyol.shoppingcart.exception.ShoppingCartException;
import com.trendyol.shoppingcart.models.dto.ItemDTO;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(itemService, "maxUniqueDigitalItemCount", 5);
        ReflectionTestUtils.setField(itemService, "maxUniqueItemCount", 10);
        ReflectionTestUtils.setField(itemService, "digitalItemCategoryId", 7889);
        ReflectionTestUtils.setField(itemService, "vasItemSellerID", 5003);
    }

    @Test
    public void givenValidItem_whenAddItem_thenItemIsAdded() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        Item item = new Item(5, 1234, 1212, 20.0, 6, null);
        when(itemRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(item);

        // When
        itemService.addItem(itemDTO);

        // Then
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void givenValidDigitalItem_whenAddItem_thenDigitalItemIsAdded() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        Item item = new Item(5, 7889, 1212, 20.0, 5, null);
        when(itemRepository.findAll()).thenReturn(new ArrayList<>());
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(item);

        // When
        itemService.addItem(itemDTO);

        // Then
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void givenInvalidPrice_whenAddItem_thenExceptionIsThrown() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        double invalidPrice = -10.0;

        // When
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(new Item(null, 7888, 1001, invalidPrice, 5, null));

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.addItem(itemDTO));
    }


    @Test
    public void givenInvalidSellerId_whenAddItem_thenExceptionIsThrown() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        int invalidSellerId = 5003;

        // When
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(new Item(null, 4321, invalidSellerId, 55, 5, null));

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.addItem(itemDTO));
    }

    @Test
    public void givenItemWithInvalidQuantity_whenAddItem_thenShoppingCartExceptionIsThrown() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        int invalidQuantity = 11;

        // When
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(new Item(null, 4321, 4444, 55, invalidQuantity, null));

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.addItem(itemDTO));
    }

    @Test
    public void givenDigitalItemWithInvalidQuantity_whenAddItem_thenShoppingCartExceptionIsThrown() {
        // Given
        ItemDTO itemDTO = new ItemDTO();
        int invalidQuantity = 6;

        // When
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(new Item(null, 7889, 4444, 55, invalidQuantity, null));

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.addItem(itemDTO));
    }

    @Test
    public void givenCartWithDefaultItem_whenAddingDigitalItem_thenShoppingCartExceptionThrown() {
        // Given:
        List<Item> cartItems = new ArrayList<>();
        Item item = new Item(5, 4321, 4321, 55, 5, null);
        cartItems.add(item);
        when(itemRepository.findAll()).thenReturn(cartItems);

        //when
        ItemDTO itemDTO = new ItemDTO();
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(new Item(null, 7889, 4444, 55, 5, null));

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.addItem(itemDTO));
    }

    @Test
    public void givenCartWithDigitalItem_whenAddingDefaultItem_thenShoppingCartExceptionThrown() {
        // Given:
        List<Item> cartItems = new ArrayList<>();
        Item item = new Item(5, 7889, 4321, 55, 5, null);
        cartItems.add(item);
        when(itemRepository.findAll()).thenReturn(cartItems);

        //when
        ItemDTO itemDTO = new ItemDTO();
        when(itemMapper.itemDTOToItem(any(ItemDTO.class))).thenReturn(new Item(null, 4321, 4444, 55, 5, null));

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.addItem(itemDTO));
    }

    @Test
    public void givenCartWithItems_whenGettingAllItems_thenAllItemsReturned() {
        // Given:
        List<Item> cartItems = new ArrayList<>();
        Item item1 = new Item(5, 9999, 4321, 55, 5, null);
        Item item2 = new Item(6, 4321, 4444, 55, 5, null);
        cartItems.add(item1);
        cartItems.add(item2);
        when(itemRepository.findAll()).thenReturn(cartItems);

        List<ItemDTO> expectedItemDTOs = new ArrayList<>();
        expectedItemDTOs.add(itemMapper.itemToItemDTO(item1));
        expectedItemDTOs.add(itemMapper.itemToItemDTO(item2));
        when(itemMapper.itemsToItemDTOs(cartItems)).thenReturn(expectedItemDTOs);

        // When
        List<ItemDTO> result = itemService.getAllItems();

        // Then
        assertEquals(cartItems.size(), result.size());
    }

    @Test
    public void givenCartWithItems_whenResettingItems_thenAllItemsDeleted() {
        // Given:
        List<Item> cartItems = new ArrayList<>();
        Item item1 = new Item(5, 9999, 4321, 55, 5, null);
        Item item2 = new Item(6, 4321, 4444, 55, 5, null);
        cartItems.add(item1);
        cartItems.add(item2);
        when(itemRepository.findAll()).thenReturn(cartItems);

        // When
        itemService.resetItems();

        // Then
        verify(itemRepository, times(1)).deleteAll();
    }

    @Test
    public void givenNonExistingItem_whenRemovingItem_thenShoppingCartExceptionThrown() {
        // Given:
        List<Item> cartItems = new ArrayList<>();
        Item item1 = new Item(1, 9999, 4321, 55, 5, null);
        when(itemRepository.findAll()).thenReturn(cartItems);

        // When
        Integer itemId = item1.getItemId();
        ItemDTO itemToRemove = new ItemDTO();
        itemToRemove.setItemId(itemId);

        // Then
        assertThrows(ShoppingCartException.class, () -> itemService.removeItem(itemToRemove));
    }

    @Test
    public void givenExistingItem_whenRemovingItem_thenItemRemoved() {
        // Given:
        Item item1 = new Item(1, 9999, 4321, 55, 5, null);
        Item item2 = new Item(2, 8888, 1234, 44, 3, null);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2)).thenReturn(Optional.of(item2));

        // When
        ItemDTO itemToRemove1 = new ItemDTO();
        itemToRemove1.setItemId(item1.getItemId());
        itemService.removeItem(itemToRemove1);

        // Then
        verify(itemRepository, times(1)).deleteById(item1.getItemId());

    }

}
