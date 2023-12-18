package com.trendyol.shoppingcart.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VasItemDTO {

    private Integer itemId;

    private int categoryId;

    private int sellerId;

    private double price;

    private int quantity;

    private Integer vasItemId;
}
