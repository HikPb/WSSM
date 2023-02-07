package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Supplier;
import com.project.wsms.repository.SupplierRepository;
import com.project.wsms.service.SupplierService;

@Service
public class SupplierServiceImpl implements SupplierService {
	
	@Autowired
	private SupplierRepository supplierRepository;

	@Override
	public List<Supplier> getAll() {
		return supplierRepository.findAll();
	}

	@Override
	public List<Supplier> getByKeyword(String key) {
		return supplierRepository.findBySupNameContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return supplierRepository.existsById(id);
	}

	@Override
	public Supplier save(Supplier supplier) {
		supplierRepository.save(supplier);
		return supplier;
	}

	@Override
	public Optional<Supplier> getById(Integer id) {
		return supplierRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		supplierRepository.deleteById(id);
	}
	

}
