package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.CheckQty;

public interface CheckQtyService {

	public List<CheckQty> getAll();
	
	//public List<CheckQty> getBySku(String key);
	
	public boolean existsById(Integer id);
	
	public CheckQty save(CheckQty item);

    public Optional<CheckQty> getById(Integer id);

    public void delete(Integer id);
}
