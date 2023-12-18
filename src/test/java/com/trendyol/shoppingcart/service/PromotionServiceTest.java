package com.trendyol.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.*;
import com.trendyol.shoppingcart.models.dto.ItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PromotionServiceTest {

    @InjectMocks
    private PromotionService promotionService;

    @BeforeEach
    public void init() {

        // PromotionConfiguration sınıfının gereksinimlerini burada doldurun, örneğin promotionConfiguration.getSameSellerPromotionRate() vb.
        ReflectionTestUtils.setField(promotionService, "totalPPMinCartAmount", 250);
        ReflectionTestUtils.setField(promotionService, "totalPPMinCartAmountOne", 5000);
        ReflectionTestUtils.setField(promotionService, "totalPPDiscountOne", 250);
        ReflectionTestUtils.setField(promotionService, "totalPPMinCartAmountTwo", 10000);
        ReflectionTestUtils.setField(promotionService, "totalPPDiscountTwo", 500);
        ReflectionTestUtils.setField(promotionService, "totalPPMinCartAmountThree", 50000);
        ReflectionTestUtils.setField(promotionService, "totalPPDiscountThree", 1000);
        ReflectionTestUtils.setField(promotionService, "totalPPDiscountFour", 2000);
        ReflectionTestUtils.setField(promotionService, "sameSellerPromotionRate", 0.1);
        ReflectionTestUtils.setField(promotionService, "categoryPromotionRate", 0.05);
        ReflectionTestUtils.setField(promotionService, "categoryPromotionTargetId", 3003);
        ReflectionTestUtils.setField(promotionService, "sameSellerPromotionRate", 0.1);
        ReflectionTestUtils.setField(promotionService, "sameSellerPromotionId", 9909);
        ReflectionTestUtils.setField(promotionService, "categoryPromotionId", 5676);
        ReflectionTestUtils.setField(promotionService, "totalPricePromotionId", 1232);
    }

    @Test
    public void testCalculateSameSellerPromotion_ShouldApplyDiscountToItemsFromTheSameSeller() {
        // Given
        List<ItemDTO> items = new ArrayList<>();

        items.add(new ItemDTO(1, 1, 12, 100, 1, null));
        items.add(new ItemDTO(2, 2, 12, 100, 1, null));

        double expectedDiscount = 20.0;

        // When
        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Then
        double discount = promotionService.calculateSameSellerPromotion(items, totalPrice);

        // Assert
        assertEquals(expectedDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateSameCategoryPromotion_ShouldApplyDiscountToItemsFromTheSameCategory() {
        // Given
        List<ItemDTO> items = new ArrayList<>();

        items.add(new ItemDTO(1, 3003, 1, 100, 1, null));
        items.add(new ItemDTO(2, 3003, 2, 100, 1, null));

        double expectedDiscount = 10.0;

        // Then
        double discount = promotionService.calculateCategoryPromotion(items);

        // Assert
        assertEquals(expectedDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateTotalPricePromotion_ShouldApplyDiscountToItemsFromTotalPriceOne() {
        // Given
        List<ItemDTO> items = new ArrayList<>();

        items.add(new ItemDTO(1, 1, 1, 100, 1, null));
        items.add(new ItemDTO(2, 2, 2, 100, 1, null));
        items.add(new ItemDTO(2, 3, 3, 100, 1, null));

        double expectedDiscount = 250.0;

        // When
        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Then
        double discount = promotionService.calculateTotalPricePromotion(totalPrice);

        // Assert
        assertEquals(expectedDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateTotalPricePromotion_ShouldApplyDiscountToItemsFromTotalPriceTwo() {
        // Given
        List<ItemDTO> items = new ArrayList<>();

        items.add(new ItemDTO(1, 1, 1, 1000, 1, null));
        items.add(new ItemDTO(2, 2, 2, 1000, 2, null));
        items.add(new ItemDTO(2, 3, 3, 1000, 3, null));

        double expectedDiscount = 500.0;

        // When
        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Then
        double discount = promotionService.calculateTotalPricePromotion(totalPrice);

        // Assert
        assertEquals(expectedDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateTotalPricePromotion_ShouldApplyDiscountToItemsFromTotalPriceThree() {
        // Given
        List<ItemDTO> items = new ArrayList<>();

        items.add(new ItemDTO(1, 1, 1, 1000, 2, null));
        items.add(new ItemDTO(2, 2, 2, 1000, 4, null));
        items.add(new ItemDTO(2, 3, 3, 1000, 6, null));

        double expectedDiscount = 1000.0;

        // When
        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Then
        double discount = promotionService.calculateTotalPricePromotion(totalPrice);

        // Assert
        assertEquals(expectedDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateTotalPricePromotion_ShouldApplyDiscountToItemsFromTotalPriceFour() {
        // Given
        List<ItemDTO> items = new ArrayList<>();

        items.add(new ItemDTO(1, 1, 1, 10000, 2, null));
        items.add(new ItemDTO(2, 2, 2, 10000, 4, null));
        items.add(new ItemDTO(2, 3, 3, 10000, 6, null));

        double expectedDiscount = 2000.0;

        // When
        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Then
        double discount = promotionService.calculateTotalPricePromotion(totalPrice);

        // Assert
        assertEquals(expectedDiscount, discount, 0.001);
    }


    @Test
    public void testCalculateDiscount_ShouldCalculateMaximumDiscountTotalPrice() {
        // Given
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(1, 3003, 12, 300, 1, null));
        items.add(new ItemDTO(2, 3003, 12, 100, 1, null));

        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        double expectedSameSellerDiscount = 20.0;
        double expectedCategoryDiscount = 10.0;
        double expectedTotalPriceDiscount = 250.0;
        double expectedTotalDiscount = Math.max(expectedSameSellerDiscount, Math.max(expectedCategoryDiscount, expectedTotalPriceDiscount));

        // When
        double discount = promotionService.calculateDiscount(items, totalPrice);

        // Then
        assertEquals(expectedTotalDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateDiscount_ShouldCalculateMaximumDiscountSameSeller() {
        // Given
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(1, 3003, 1, 25000, 1, null));
        items.add(new ItemDTO(2, 3003, 1, 25000, 1, null));

        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        double expectedSameSellerDiscount = 5000.0;
        double expectedCategoryDiscount = 2500.0;
        double expectedTotalPriceDiscount = 2000.0;
        double expectedTotalDiscount = Math.max(expectedSameSellerDiscount, Math.max(expectedCategoryDiscount, expectedTotalPriceDiscount));

        // When
        double discount = promotionService.calculateDiscount(items, totalPrice);

        // Then
        assertEquals(expectedTotalDiscount, discount, 0.001);
    }

    @Test
    public void testCalculateDiscount_ShouldCalculateMaximumDiscountSameCategory() {
        // Given
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(1, 3003, 1, 25000, 1, null));
        items.add(new ItemDTO(2, 3003, 12, 25000, 1, null));

        double totalPrice = 0.0;
        for (ItemDTO item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        double expectedSameSellerDiscount = 0;
        double expectedCategoryDiscount = 2500.0;
        double expectedTotalPriceDiscount = 2000.0;
        double expectedTotalDiscount = Math.max(expectedSameSellerDiscount, Math.max(expectedCategoryDiscount, expectedTotalPriceDiscount));

        // When
        double discount = promotionService.calculateDiscount(items, totalPrice);

        // Then
        assertEquals(expectedTotalDiscount, discount, 0.001);
    }

}
