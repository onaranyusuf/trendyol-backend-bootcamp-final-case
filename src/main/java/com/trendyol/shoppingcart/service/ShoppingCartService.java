package com.trendyol.shoppingcart.service;

import com.trendyol.shoppingcart.exception.ShoppingCartException;
import com.trendyol.shoppingcart.models.dto.ItemDTO;
import com.trendyol.shoppingcart.models.dto.ResponseDTO;
import com.trendyol.shoppingcart.models.dto.ShoppingCartDTO;
import com.trendyol.shoppingcart.models.dto.VasItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService {

    private final VasItemService vasItemService;

    private final ItemService itemService;

    private final PromotionService promotionService;

    @Value("${max.total.item.count}")
    private int maxTotalItemCount;

    @Value("${max.total.cart.amount}")
    private double maxTotalCartAmount;

    private int calculateTotalItemCount(List<ItemDTO> itemDTOs) {
        int totalItemCount = 0;
        for (ItemDTO itemDTO : itemDTOs) {
            totalItemCount += itemDTO.getQuantity();
        }
        return totalItemCount;
    }

    @Transactional
    public ResponseDTO<String> addItemToShoppingCart(ItemDTO itemDTO) {
        validateNewItem(itemDTO.getPrice(), itemDTO.getQuantity());

        itemService.addItem(itemDTO);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(true);
        responseDTO.setMessage("Item has been added to the shopping cart successfully");
        return responseDTO;
    }

    @Transactional
    public ResponseDTO<String> addVasItemToShoppingCart(VasItemDTO vasItemDTO) {
        validateNewItem(vasItemDTO.getPrice(), vasItemDTO.getQuantity());

        vasItemService.addVasItem(vasItemDTO);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(true);
        responseDTO.setMessage("Vas Item has been added to shopping cart successfully");
        return responseDTO;
    }

    private void validateNewItem(double price, int quantity) {
        List<ItemDTO> itemDTOs = itemService.getAllItems();
        int totalItemCount = calculateTotalItemCount(itemDTOs);
        long totalVasItemCount = vasItemService.countVasItems();

        if (totalItemCount + totalVasItemCount + quantity > maxTotalItemCount) {
            throw new ShoppingCartException("Total item count in the shopping cart cannot exceed 30.");
        }
        double totalPrice = calculateTotalPrice(itemDTOs);
        double discount = promotionService.calculateDiscount(itemDTOs, totalPrice);
        totalPrice = totalPrice - discount + (price * quantity);

        if (totalPrice > maxTotalCartAmount) {
            throw new ShoppingCartException("Total price in the shopping cart cannot exceed 500000 TL.");
        }
    }

    public ResponseDTO<ShoppingCartDTO> getShoppingCart() {

        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder().build();

        List<ItemDTO> itemDTOs = itemService.getAllItems();
        shoppingCartDTO.setItems(itemDTOs);

        double totalPrice = calculateTotalPrice(itemDTOs);
        promotionService.setPromotions(shoppingCartDTO, itemDTOs, totalPrice);

        totalPrice -= shoppingCartDTO.getTotalDiscount();
        shoppingCartDTO.setTotalPrice(totalPrice);

        ResponseDTO<ShoppingCartDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(true);
        responseDTO.setMessage(shoppingCartDTO);

        return responseDTO;
    }

    private double calculateTotalPrice(List<ItemDTO> items) {
        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
            for (VasItemDTO vasItemDTO : item.getVasItems()) {
                totalPrice += vasItemDTO.getPrice() * vasItemDTO.getQuantity();
            }
        }
        return totalPrice;
    }

    public ResponseDTO<String> resetShoppingCart() {
        itemService.resetItems();
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(true);
        responseDTO.setMessage("Shopping cart has been reset successfully");
        return responseDTO;
    }

    public ResponseDTO<String> removeItemFromShoppingCart(ItemDTO itemDTO) {
        itemService.removeItem(itemDTO);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(true);
        responseDTO.setMessage("Item has been removed from shopping cart successfully");
        return responseDTO;
    }

    public ResponseDTO<String> removeVasItemFromShoppingCart(VasItemDTO vasItemDTO) {
        vasItemService.removeVasItem(vasItemDTO);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(true);
        responseDTO.setMessage("Vas Item has been removed from shopping cart successfully");
        return responseDTO;
    }
}
