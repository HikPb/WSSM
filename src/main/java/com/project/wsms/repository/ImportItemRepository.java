package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.ImportItem;

public interface ImportItemRepository extends JpaRepository<ImportItem, Integer> {
	List<ImportItem> findBySkuContains(String sku);
}
