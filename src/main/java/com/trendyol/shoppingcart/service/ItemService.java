package com.trendyol.shoppingcart.service;

import com.trendyol.shoppingcart.exception.ShoppingCartException;
import com.trendyol.shoppingcart.mapper.ItemMapper;
import com.trendyol.shoppingcart.models.dto.ItemDTO;
import com.trendyol.shoppingcart.models.entity.Item;
import com.trendyol.shoppingcart.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Value("${max.unique.item.count}")
    int maxUniqueItemCount;

    @Value("${quantity.zero}")
    int quantityZero;

    @Value("${vas.item.seller.id}")
    int vasItemSellerID;

    @Value("${digital.item.category.id}")
    int digitalItemCategoryId;

    @Value("${max.unique.digital.item.count}")
    int maxUniqueDigitalItemCount;

    @Transactional
    public void addItem(ItemDTO itemDTO) {
        Item item = itemMapper.itemDTOToItem(itemDTO);
        item.setItemId(null);
        List<Item> allItems = itemRepository.findAll();

        if (item.getSellerId() == vasItemSellerID) { throw new ShoppingCartException("Item sellerId cannot be 5003."); }
        if (item.getPrice() < quantityZero) { throw new ShoppingCartException("Item price cannot be lower than 0."); }

        if (item.getCategoryId() == digitalItemCategoryId) {
            validateDigitalItemAddition(allItems, item);
        } else {
            validateDefaultItemAddition(allItems, item);
        }
        itemRepository.save(item);
    }

    private void validateDigitalItemAddition(List<Item> cartItems, Item item) {
        if (cartItems.stream().anyMatch(cartItem -> cartItem.getCategoryId() != digitalItemCategoryId)) {
            throw new ShoppingCartException("Digital items cannot be added to a cart with Default Items.");
        }
        if (item.getQuantity() <= quantityZero || item.getQuantity() > maxUniqueDigitalItemCount) {
            throw new ShoppingCartException("Digital Item quantity cannot be zero, negative or upper than 5.");
        }
    }

    private void validateDefaultItemAddition(List<Item> cartItems, Item item) {
        if (cartItems.stream().anyMatch(cartItem -> cartItem.getCategoryId() == digitalItemCategoryId)) {
            throw new ShoppingCartException("Only Digital Items can be added to a cart with digital items.");
        }
        if (item.getQuantity() <= quantityZero || item.getQuantity() > maxUniqueItemCount) {
            throw new ShoppingCartException("Item quantity cannot be zero, negative or upper than 10.");
        }
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return itemMapper.itemsToItemDTOs(items);
    }

    public void resetItems() {
        itemRepository.deleteAll();
    }

    public void removeItem(ItemDTO itemDTO) {
        int itemId = itemDTO.getItemId();
        itemRepository.findById(itemId)
                .orElseThrow(() -> new ShoppingCartException("'itemId: " + itemId + "' not found!"));
        itemRepository.deleteById(itemId);
    }

}
