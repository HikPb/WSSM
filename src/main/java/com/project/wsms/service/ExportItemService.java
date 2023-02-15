package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.ExportItem;

public interface ExportItemService {

	public List<ExportItem> getAll();
	
	public List<ExportItem> getBySku(String key);
	
	public boolean existsById(Integer id);
	
	public ExportItem save(ExportItem item);

    public Optional<ExportItem> getById(Integer id);

    public void delete(Integer id);
}
