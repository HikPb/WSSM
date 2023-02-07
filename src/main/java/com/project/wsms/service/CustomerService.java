package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Customer;

public interface CustomerService {

    public Customer save(Customer customer);
    
    public void delete(Integer id);
    
    public List<Customer> getAll();

    public List<Customer> getByName(String name);
    
    public List<Customer> getByPhone(String phone);
	
	public boolean existsById(Integer id);

    public Optional<Customer> getById(Integer id);

}
