package com.trendyol.shoppingcart.repository;

import com.trendyol.shoppingcart.models.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

}
