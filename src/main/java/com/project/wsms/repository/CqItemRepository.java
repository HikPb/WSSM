package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.CqItem;

public interface CqItemRepository extends JpaRepository<CqItem, Integer> {
	List<CqItem> findBySkuContains(String sku);

}
