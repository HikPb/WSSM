package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Order;

public interface OrderService{

	public void save(Order order);
		
	public boolean existsById(Integer id);

    public List<Order> getAll();

	public List<Order> getByCustomer(Integer id);

    public void delete(Integer orderId);	
	
	public Optional<Order> getById(Integer orderId);
}
