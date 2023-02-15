package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.ImportItem;

public interface ImportItemService {

	public List<ImportItem> getAll();
	
	public List<ImportItem> getBySku(String key);
	
	public boolean existsById(Integer id);
	
	public ImportItem save(ImportItem item);

    public Optional<ImportItem> getById(Integer id);

    public void delete(Integer id);
}
