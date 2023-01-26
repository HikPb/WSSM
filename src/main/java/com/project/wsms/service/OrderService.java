package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Order;

public interface OrderService{

	public void save(Order order);
	
	public void update(Order order);
	
//	public void updateIsSell(String orderId, Boolean isSell);

    public List<Order> getAll();

    public void delete(String orderId);

//	public Page<Order> getAll(Pageable paging);
	
	
	public Optional<Order> getByOrderId(String orderId);
}
