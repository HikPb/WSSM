package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Export;

public interface ExportService {
	
	public void save(Export epform);

    public List<Export> getAll();
    
    public boolean existsById(Integer id);

    public void delete(Integer id);

	public Optional<Export> getById(Integer id);
}
