package com.trendyol.shoppingcart.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private Integer itemId;

    private int categoryId;

    private int sellerId;

    private double price;

    private int quantity;

    private List<VasItemDTO> vasItems;
}
