package com.trendyol.shoppingcart.models.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VasItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    private int categoryId;

    private int sellerId;

    private double price;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item defaultItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VasItem)) return false;
        return itemId != null && itemId.equals(((VasItem) o).getItemId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
