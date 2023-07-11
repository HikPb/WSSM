package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.ERole;
import com.project.wsms.model.Employee;

public interface EmployeeService {
	
	public void save(Employee employee);

    public List<Employee> getAll();
    
    public boolean existsByUsername(String username);

    public boolean existsById(Integer id);

    public void delete(Integer id);

	public Optional<Employee> getById(Integer id);

    public Optional<Employee> getByUsername(String username);

    public List<Employee> getByRole(ERole roleName);
}
