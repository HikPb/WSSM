package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Employee;
import com.project.wsms.repository.EmployeeRepository;
import com.project.wsms.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepository emRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public void save(Employee employee) {
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		emRepository.save(employee);
	}

	@Override
	public List<Employee> getAll() {
		return emRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return emRepository.existsById(id);
	}

	@Override
	public void delete(Integer id) {
		emRepository.deleteById(id);
		
	}

	@Override
	public Optional<Employee> getById(Integer id) {
		return emRepository.findById(id);
	}
	
	@Override
	public Optional<Employee> getByUsername(String username) {
		return emRepository.findByUsername(username);
	}
}
