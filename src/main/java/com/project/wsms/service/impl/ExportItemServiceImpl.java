package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.ExportItem;
import com.project.wsms.repository.ExportItemRepository;
import com.project.wsms.service.ExportItemService;

@Service
public class ExportItemServiceImpl implements ExportItemService {
	
	@Autowired
	private ExportItemRepository itemRepository;

	@Override
	public List<ExportItem> getAll() {
		return itemRepository.findAll();
	}

	@Override
	public List<ExportItem> getBySku(String key) {
		return itemRepository.findBySkuContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return itemRepository.existsById(id);
	}

	@Override
	public ExportItem save(ExportItem item) {
		itemRepository.save(item);
		return item;
	}

	@Override
	public Optional<ExportItem> getById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		itemRepository.deleteById(id);
	}
	

}
