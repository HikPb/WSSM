package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	List<Item> findByNameContains(String name);

    List<Item> getItemInWarehouse(Integer wareId);
}
