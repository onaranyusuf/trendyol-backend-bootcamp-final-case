package com.trendyol.shoppingcart.service;

import com.trendyol.shoppingcart.models.dto.ItemDTO;
import com.trendyol.shoppingcart.models.dto.ShoppingCartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    @Value("${same.seller.promotion.id}")
    private int sameSellerPromotionId;

    @Value("${category.promotion.id}")
    private int categoryPromotionId;

    @Value("${total.price.promotion.id}")
    private int totalPricePromotionId;

    @Value("${promotion.totalPPMinCartAmount}")
    private int totalPPMinCartAmount;

    @Value("${promotion.totalPPMinCartAmountOne}")
    private int totalPPMinCartAmountOne;

    @Value("${promotion.totalPPDiscountOne}")
    private int totalPPDiscountOne;

    @Value("${promotion.totalPPMinCartAmountTwo}")
    private int totalPPMinCartAmountTwo;

    @Value("${promotion.totalPPDiscountTwo}")
    private int totalPPDiscountTwo;

    @Value("${promotion.totalPPMinCartAmountThree}")
    private int totalPPMinCartAmountThree;

    @Value("${promotion.totalPPDiscountThree}")
    private int totalPPDiscountThree;

    @Value("${promotion.totalPPDiscountFour}")
    private int totalPPDiscountFour;

    @Value("${promotion.sameSellerPromotionRate}")
    private double sameSellerPromotionRate;

    @Value("${promotion.categoryPromotionRate}")
    private double categoryPromotionRate;

    @Value("${promotion.categoryPromotionTargetId}")
    private long categoryPromotionTargetId;

    public void setPromotions(ShoppingCartDTO shoppingCartDTO, List<ItemDTO> items, double totalPrice) {
        double totalDiscount;
        double sameSellerDiscount = calculateSameSellerPromotion(items, totalPrice);
        double categoryDiscount = calculateCategoryPromotion(items);
        double totalPricePromotion = calculateTotalPricePromotion(totalPrice);

        totalDiscount = Math.max(sameSellerDiscount, Math.max(categoryDiscount, totalPricePromotion));
        shoppingCartDTO.setTotalDiscount(calculateDiscount(items, totalPrice));

        int mostDiscountedPromotionId;
        if (totalDiscount == sameSellerDiscount && sameSellerDiscount != 0.0) {
            mostDiscountedPromotionId = sameSellerPromotionId;
        } else if (totalDiscount == categoryDiscount && categoryDiscount != 0.0) {
            mostDiscountedPromotionId = categoryPromotionId;
        } else if (totalDiscount == totalPricePromotion && totalPricePromotion != 0.0) {
            mostDiscountedPromotionId = totalPricePromotionId;
        } else {
            mostDiscountedPromotionId = 0;
        }
        shoppingCartDTO.setAppliedPromotionId(mostDiscountedPromotionId);
    }

    public double calculateDiscount(List<ItemDTO> items, double totalPrice) {
        double totalDiscount;
        double sameSellerDiscount = calculateSameSellerPromotion(items, totalPrice);
        double categoryDiscount = calculateCategoryPromotion(items);
        double totalPricePromotion = calculateTotalPricePromotion(totalPrice);

        totalDiscount = Math.max(sameSellerDiscount, Math.max(categoryDiscount, totalPricePromotion));
        return  totalDiscount;
    }

    public double calculateSameSellerPromotion(List<ItemDTO> items, double totalPrice) {
        double sameSellerDiscount = 0.0;

        if (!items.isEmpty()) {
            long sellerId = items.get(0).getSellerId();
            boolean sameSeller = true;

            for (ItemDTO item : items) {
                if (item.getSellerId() != sellerId) {
                    sameSeller = false;
                    break;
                }
            }
            if (sameSeller && items.size() > 1) {
                sameSellerDiscount = totalPrice * sameSellerPromotionRate;
            } else {
                sameSellerDiscount = 0.0;
            }
        }

        return sameSellerDiscount;
    }

    public double calculateCategoryPromotion(List<ItemDTO> items) {
        double categoryDiscount = 0.0;

        for (ItemDTO item : items) {
            if (item.getCategoryId() == categoryPromotionTargetId) {
                categoryDiscount += (item.getPrice() * item.getQuantity()) * categoryPromotionRate;
            }
        }
        return categoryDiscount;
    }

    public double calculateTotalPricePromotion(double totalPrice) {
        double totalPricePromotion = 0.0;
        if (totalPPMinCartAmountOne > totalPrice && totalPrice >= totalPPMinCartAmount) {
            totalPricePromotion = totalPPDiscountOne;
        } else if (totalPPMinCartAmountTwo > totalPrice && totalPrice >= totalPPMinCartAmountOne) {
            totalPricePromotion = totalPPDiscountTwo;
        } else if (totalPPMinCartAmountThree > totalPrice && totalPrice >= totalPPMinCartAmountTwo) {
            totalPricePromotion = totalPPDiscountThree;
        } else if (totalPrice >= totalPPMinCartAmountThree) {
            totalPricePromotion = totalPPDiscountFour;
        }
        return totalPricePromotion;
    }

}
