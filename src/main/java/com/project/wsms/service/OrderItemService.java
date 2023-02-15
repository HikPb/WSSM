package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.OrderItem;

public interface OrderItemService {

	public List<OrderItem> getAll();
	
	public List<OrderItem> getBySku(String key);
	
	public boolean existsById(Integer id);
	
	public OrderItem save(OrderItem item);

    public Optional<OrderItem> getById(Integer id);

    public void delete(Integer id);
}
