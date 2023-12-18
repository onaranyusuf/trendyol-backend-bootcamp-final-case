package com.trendyol.shoppingcart.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = { @Index(columnList = "categoryId") })
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    private int categoryId;

    private int sellerId;

    private double price;

    private int quantity;

    @OneToMany(mappedBy = "defaultItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private Set<VasItem> vasItems = new HashSet<>();

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
