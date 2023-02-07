package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
	List<Supplier> findBySupNameContains(String supName);
}
