package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.wsms.model.Order;

public interface OrderService{

	public void save(Order order);
		
	public boolean existsById(Integer id);

    public List<Order> getAll();

	public Page<Order> getAll(Pageable paging);

	public Page<Order> search(String keyword, Pageable paging);

	public List<Order> getByCustomer(Integer id);

	public List<Order> getByStatusIn(List<Integer> listStatus);

    public void delete(Integer orderId);	
	
	public Optional<Order> getById(Integer orderId);
}
