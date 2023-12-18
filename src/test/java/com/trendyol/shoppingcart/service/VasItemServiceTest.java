package com.trendyol.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import com.trendyol.shoppingcart.exception.ShoppingCartException;
import com.trendyol.shoppingcart.models.dto.VasItemDTO;
import com.trendyol.shoppingcart.models.entity.Item;
import com.trendyol.shoppingcart.models.entity.VasItem;
import com.trendyol.shoppingcart.repository.ItemRepository;
import com.trendyol.shoppingcart.repository.VasItemRepository;
import com.trendyol.shoppingcart.mapper.VasItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VasItemServiceTest {


    @InjectMocks
    private VasItemService vasItemService;

    @Mock
    private VasItemRepository vasItemRepository;

    @Mock
    private VasItemMapper vasItemMapper;

    @Mock
    private ItemRepository itemRepository;


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(vasItemService, "vasItemSellerID", 5003);
        ReflectionTestUtils.setField(vasItemService, "maxVasCountDefaultItem", 3);
        ReflectionTestUtils.setField(vasItemService, "vasItemCategoryId", 3242);
        ReflectionTestUtils.setField(vasItemService, "quantityZero", 0);
        List<Integer> newValues = Arrays.asList(1001, 3004);
        ReflectionTestUtils.setField(vasItemService, "vasItemCanBeAddedTo", newValues);
    }

    @Test
    public void givenValidVasItem_whenAddVasItem_thenVasItemIsAdded() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(2);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 5003, 10.0, 2, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When
        vasItemService.addVasItem(vasItemDTO);

        // Then
        verify(vasItemRepository, times(1)).save(any(VasItem.class));
    }

    @Test
    public void givenVasItemWithInvalidCategoryId_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3);
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(2);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3, 5003, 10.0, 2, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenVasItemWithInvalidSellerId_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(1);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(2);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 1, 10.0, 2, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenVasItemWithHigherPriceThanPriceOfItem_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(30.0);
        vasItemDTO.setQuantity(2);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 5003, 30.0, 2, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenVasItemInvalidPrice_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(-10);
        vasItemDTO.setQuantity(2);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 5003, -10, 2, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenVasItemInvalidQuantity_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(-2);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 5003, 10.0, -2, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenVasItemHigherQuantityThanAllowed_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(4);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1001, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 5003, 10.0, 4, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenValidVasItemWithNotValidCategoryIdOfItem_whenAddVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setSellerId(5003);
        vasItemDTO.setCategoryId(3242);
        vasItemDTO.setPrice(10.0);
        vasItemDTO.setQuantity(3);
        vasItemDTO.setItemId(1);

        Item item = new Item(1, 1, 5003, 20.0, 6, new HashSet<>());
        VasItem vasItem = new VasItem(1, 3242, 5003, 10.0, 3, item);

        when(vasItemRepository.findAll()).thenReturn(new ArrayList<>());
        when(vasItemMapper.vasItemDTOToVasItem(any(VasItemDTO.class))).thenReturn(vasItem);
        when(itemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(item));

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.addVasItem(vasItemDTO));
    }

    @Test
    public void givenVasItems_whenCountVasItems_thenVasItemsCounted() {
        // Given
        List<VasItem> vasItems = new ArrayList<>();
        VasItem vasItem1 = new VasItem(1, 3242, 5003, 10.0, 3, null);
        vasItems.add(vasItem1);
        VasItem vasItem2 = new VasItem(2, 3242, 5003, 15.0, 2, null);
        vasItems.add(vasItem2);

        when(vasItemRepository.count()).thenReturn((long) vasItems.size());

        // When
        long vasItemsCount = vasItemService.countVasItems();

        // Then
        assertEquals(2L, vasItemsCount);
    }

    @Test
    public void givenVasItem_whenRemoveVasItem_thenVasItemIsRemoved() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setItemId(1);

        VasItem vasItem = new VasItem(1, 3242, 5003, 10.0, 3, null);
        when(vasItemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.of(vasItem));

        // When
        vasItemService.removeVasItem(vasItemDTO);

        // Then
        verify(vasItemRepository, times(1)).delete(vasItem);
    }

    @Test
    public void givenVasItemWithInvalidItemId_whenRemoveVasItem_thenShoppingCartExceptionThrown() {
        // Given
        VasItemDTO vasItemDTO = new VasItemDTO();
        vasItemDTO.setItemId(1);

        when(vasItemRepository.findById(vasItemDTO.getItemId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ShoppingCartException.class, () -> vasItemService.removeVasItem(vasItemDTO));
    }

}
