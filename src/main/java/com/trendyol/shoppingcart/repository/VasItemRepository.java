package com.trendyol.shoppingcart.repository;

import com.trendyol.shoppingcart.models.entity.VasItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VasItemRepository extends JpaRepository<VasItem, Integer> {

}
