package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.ExportItem;

public interface ExportItemRepository extends JpaRepository<ExportItem, Integer> {
	List<ExportItem> findBySkuContains(String sku);

}
