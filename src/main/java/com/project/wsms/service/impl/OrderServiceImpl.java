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
	public void delete(Integer orderId) {
		orderRepository.deleteById(orderId);	
	}

	@Override
	public Optional<Order> getById(Integer orderId) {
		return orderRepository.findById(orderId);
	}

}
