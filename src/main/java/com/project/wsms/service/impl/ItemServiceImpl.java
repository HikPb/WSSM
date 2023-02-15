package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Item;
import com.project.wsms.repository.ItemRepository;
import com.project.wsms.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemRepository itemRepository;

	@Override
	public List<Item> getAll() {
		return itemRepository.findAll();
	}

	@Override
	public List<Item> getBySku(String key) {
		return itemRepository.findBySkuContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return itemRepository.existsById(id);
	}

	@Override
	public Item save(Item item) {
		itemRepository.save(item);
		return item;
	}

	@Override
	public Optional<Item> getById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		itemRepository.deleteById(id);
	}

	@Override
	public List<Item> getByWareId(Integer wareId) {
		return itemRepository.findByWarehouse_id(wareId);
	}
	
	@Override
	public List<Item> getByProductId(Integer id) {
		return itemRepository.findByProduct_id(id);
	}

	@Override
	public Item getByWareIdProductId(Integer wareId, Integer productId) {
		return itemRepository.findByWarehouse_idAndProduct_id(wareId, productId);
	}
}
