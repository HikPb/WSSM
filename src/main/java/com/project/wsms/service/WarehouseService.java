package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Warehouse;

public interface WarehouseService {

	public List<Warehouse> getAll();
	
	public List<Warehouse> getByKeyword(String key);
	
	public boolean existsById(Integer id);
	
	public Warehouse save(Warehouse warehouse);

    public Optional<Warehouse> getById(Integer id);

    public void delete(Integer id);
}
