package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.CqItem;
import com.project.wsms.repository.CqItemRepository;
import com.project.wsms.service.CqItemService;

@Service
public class CqItemServiceImpl implements CqItemService {
	
	@Autowired
	private CqItemRepository itemRepository;

	@Override
	public List<CqItem> getAll() {
		return itemRepository.findAll();
	}

	@Override
	public List<CqItem> getBySku(String key) {
		return itemRepository.findBySkuContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return itemRepository.existsById(id);
	}

	@Override
	public CqItem save(CqItem item) {
		itemRepository.save(item);
		return item;
	}

	@Override
	public Optional<CqItem> getById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		itemRepository.deleteById(id);
	}
	

}
