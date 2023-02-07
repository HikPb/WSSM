package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Customer;
import com.project.wsms.repository.CustomerRepository;
import com.project.wsms.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository cusRepository;

	@Override
	public Customer save(Customer customer) {
		return cusRepository.save(customer);
	}

	@Override
	public void delete(Integer id) {
		cusRepository.deleteById(id);
	}

	@Override
	public List<Customer> getAll() {
		return cusRepository.findAll();
	}

	@Override
	public List<Customer> getByName(String name) {
		return cusRepository.findByNameContains(name);
	}

	@Override
	public List<Customer> getByPhone(String phone) {
		return cusRepository.findByPhoneContains(phone);
	}

	@Override
	public boolean existsById(Integer id) {
		return cusRepository.existsById(id);
	}

	@Override
	public Optional<Customer> getById(Integer id) {
		return cusRepository.findById(id);
	}
}
