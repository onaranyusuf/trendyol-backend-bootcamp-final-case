package com.trendyol.shoppingcart.service;

import com.trendyol.shoppingcart.exception.ShoppingCartException;
import com.trendyol.shoppingcart.mapper.VasItemMapper;
import com.trendyol.shoppingcart.models.dto.VasItemDTO;
import com.trendyol.shoppingcart.models.entity.Item;
import com.trendyol.shoppingcart.models.entity.VasItem;
import com.trendyol.shoppingcart.repository.ItemRepository;
import com.trendyol.shoppingcart.repository.VasItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VasItemService {

    private final VasItemRepository vasItemRepository;

    private final ItemRepository itemRepository;

    private final VasItemMapper vasItemMapper;

    @Value("${vas.item.seller.id}")
    int vasItemSellerID;

    @Value("${default.item.max.vas.count}")
    int maxVasCountDefaultItem;

    @Value("${vas.item.category.id}")
    int vasItemCategoryId;

    @Value("${vas.item.can.be.added.to.category.id}")
    private List<Integer> vasItemCanBeAddedTo;

    @Value("${quantity.zero}")
    int quantityZero;

    @Transactional
    public void addVasItem(VasItemDTO vasItemDTO) {
        VasItem vasItem = vasItemMapper.vasItemDTOToVasItem(vasItemDTO);

        Item item = itemRepository.findById(vasItemDTO.getItemId())
                .orElseThrow(() -> new ShoppingCartException("'itemId: " + vasItemDTO.getItemId() + "' not found!"));

        if (!vasItemCanBeAddedTo.contains(item.getCategoryId())) {
            throw new ShoppingCartException("VasItem can only be added to items with allowed category IDs (1001 or 3004).");
        }

        if (vasItem.getCategoryId() != vasItemCategoryId) { throw new ShoppingCartException("VasItem categoryId must be 3242."); }

        if (vasItem.getSellerId() != vasItemSellerID) { throw new ShoppingCartException("VasItem sellerId must be 5003."); }

        if (vasItem.getPrice() > item.getPrice()) { throw new ShoppingCartException("VasItem price cannot be higher than the Item price."); }

        if (vasItem.getPrice() < quantityZero) { throw new ShoppingCartException("VasItem price cannot be lower than 0."); }

        if (vasItem.getQuantity() <= quantityZero) { throw new ShoppingCartException("VasItem quantity cannot be zero or negative."); }

        int totalVasItemCount = item.getVasItems().stream().mapToInt(VasItem::getQuantity).sum();
        if (totalVasItemCount + vasItemDTO.getQuantity() > maxVasCountDefaultItem) {
            throw new ShoppingCartException("Maximum of 3 VasItems per Item is allowed.");
        }

        vasItem.setItemId(null);
        vasItem.setDefaultItem(item);
        item.getVasItems().add(vasItem);
        vasItemRepository.save(vasItem);
    }

    @Transactional(readOnly = true)
    public long countVasItems() {
        return vasItemRepository.count();
    }

    public void removeVasItem(VasItemDTO vasItemDTO) {
        int itemId = vasItemDTO.getItemId();
        VasItem vasItem = vasItemRepository.findById(itemId)
                .orElseThrow(() -> new ShoppingCartException("'itemId: " + itemId + "' not found!"));
        vasItemRepository.delete(vasItem);
    }

}
