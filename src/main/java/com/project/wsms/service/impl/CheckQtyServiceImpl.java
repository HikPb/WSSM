package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.CheckQty;
import com.project.wsms.repository.CheckQtyRepository;
import com.project.wsms.service.CheckQtyService;

@Service
public class CheckQtyServiceImpl implements CheckQtyService {

	@Autowired
	private CheckQtyRepository itemRepository;

	@Override
	public List<CheckQty> getAll() {
		return itemRepository.findAll();
	}

	/*
	 * @Override public List<CheckQty> getBySku(String key) { return
	 * itemRepository.findBySkuContains(key); }
	 */

	@Override
	public boolean existsById(Integer id) {
		return itemRepository.existsById(id);
	}

	@Override
	public CheckQty save(CheckQty item) {
		itemRepository.save(item);
		return item;
	}

	@Override
	public Optional<CheckQty> getById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		itemRepository.deleteById(id);
	}
}
