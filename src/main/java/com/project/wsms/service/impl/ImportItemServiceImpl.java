package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.ImportItem;
import com.project.wsms.repository.ImportItemRepository;
import com.project.wsms.service.ImportItemService;

@Service
public class ImportItemServiceImpl implements ImportItemService {
	
	@Autowired
	private ImportItemRepository itemRepository;

	@Override
	public List<ImportItem> getAll() {
		return itemRepository.findAll();
	}

	@Override
	public List<ImportItem> getBySku(String key) {
		return itemRepository.findBySkuContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return itemRepository.existsById(id);
	}

	@Override
	public ImportItem save(ImportItem item) {
		itemRepository.save(item);
		return item;
	}

	@Override
	public Optional<ImportItem> getById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		itemRepository.deleteById(id);
	}
	

}
