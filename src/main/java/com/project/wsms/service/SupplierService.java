package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Supplier;

public interface SupplierService {

	public List<Supplier> getAll();
	
	public List<Supplier> getByKeyword(String key);
	
	public boolean existsById(Integer id);
	
	public Supplier save(Supplier supplier);

    public Optional<Supplier> getById(Integer id);

    public void delete(Integer id);
}
