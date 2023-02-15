package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.CqItem;

public interface CqItemService {

	public List<CqItem> getAll();
	
	public List<CqItem> getBySku(String key);
	
	public boolean existsById(Integer id);
	
	public CqItem save(CqItem item);

    public Optional<CqItem> getById(Integer id);

    public void delete(Integer id);
}
