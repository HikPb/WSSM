package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.OrderItem;
import com.project.wsms.repository.OrderItemRepository;
import com.project.wsms.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	
	@Autowired
	private OrderItemRepository itemRepository;

	@Override
	public List<OrderItem> getAll() {
		return itemRepository.findAll();
	}

	@Override
	public List<OrderItem> getBySku(String key) {
		return itemRepository.findBySkuContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return itemRepository.existsById(id);
	}

	@Override
	public OrderItem save(OrderItem item) {
		itemRepository.save(item);
		return item;
	}

	@Override
	public Optional<OrderItem> getById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		itemRepository.deleteById(id);
	}
	

}
