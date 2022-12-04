package com.project.wsms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.collection.Customer;
import com.project.wsms.repository.CustomerRepository;
import com.project.wsms.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository cusRepository;
	
//	@Override
//    public String save(Customer customer) {
//        return cusRepository.save(customer).getIdCus();
//    }
//
//    @Override
//    public List<Customer> getCustomerByName(String name) {
//        return cusRepository.findCustomerByName(name);
//    }
//
//    @Override
//    public void delete(String id) {
//        cusRepository.deleteById(id);
//    }
//
//    @Override
//    public List<Customer> getCustomerByPhone(String phone) {
//        return cusRepository.findCustomerByPhone(phone);
//    }
}
