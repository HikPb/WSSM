package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	List<Item> findBySkuContains(String sku);
    List<Item> findByWarehouse_id(Integer id);
    List<Item> findByProduct_id(Integer id);
    Item findByWarehouse_idAndProduct_id(Integer wareId, Integer productId);

}
