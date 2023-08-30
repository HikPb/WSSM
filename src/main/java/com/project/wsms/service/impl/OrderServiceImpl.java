package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Order;
import com.project.wsms.repository.OrderRepository;
import com.project.wsms.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public void save(Order order) {
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
	public boolean existsById(Integer id) {
		return orderRepository.existsById(id);	
	}

	@Override
	public Optional<Order> getById(Integer orderId) {
		return orderRepository.findById(orderId);
	}

	@Override
	public List<Order> getByCustomer(Integer id) {
		return orderRepository.findByCustomer_id(id);
	}

	@Override
	public List<Order> getByStatusIn(List<Integer> listStatus) {
		return orderRepository.findByStatusIn(listStatus);
	}

	@Override
	public Page<Order> getAll(Pageable paging) {
		return orderRepository.findAll(paging);
	}

	@Override
	public Page<Order> search(String keyword, Pageable paging) {
		return orderRepository.findByCustomer_nameContainsOrCustomer_phoneContainsOrAddressContains(keyword, keyword, keyword, paging);
	}

}
