package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.wsms.model.Order;
import com.project.wsms.repository.OrderRepository;
import com.project.wsms.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	@Override
	public void save(Order order) {
		orderRepository.save(order);
	}

	@Override
	public void update(Order order) {
		orderRepository.save(order);
	}

	@Override
	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	@Override
	public void delete(String orderId) {
		orderRepository.deleteById(orderId);	
	}

//	@Override
//	public Page<Order> getAll(Pageable paging) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	@Override
	public Optional<Order> getByOrderId(String orderId) {
		return orderRepository.findById(orderId);
	}

}
