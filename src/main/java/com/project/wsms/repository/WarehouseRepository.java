package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
	List<Warehouse> findByNameContains(String name);
}
