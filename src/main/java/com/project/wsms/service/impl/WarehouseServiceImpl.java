package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Warehouse;
import com.project.wsms.repository.WarehouseRepository;
import com.project.wsms.service.WarehouseService;
@Service
public class WarehouseServiceImpl implements WarehouseService{

	@Autowired
	private WarehouseRepository wareRepository;

	@Override
	public List<Warehouse> getAll() {
		return wareRepository.findAll();
	}

	@Override
	public List<Warehouse> getByKeyword(String key) {
		return wareRepository.findByNameContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return wareRepository.existsById(id);
	}

	@Override
	public Warehouse save(Warehouse warehouse) {
		return wareRepository.save(warehouse);
	}

	@Override
	public Optional<Warehouse> getById(Integer id) {
		return wareRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		wareRepository.deleteById(id);
	}
}
