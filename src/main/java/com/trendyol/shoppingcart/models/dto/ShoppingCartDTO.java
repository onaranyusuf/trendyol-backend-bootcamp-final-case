package com.trendyol.shoppingcart.models.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {

    private List<ItemDTO> items;

    private double totalPrice;

    private int appliedPromotionId;

    private double totalDiscount;

}
