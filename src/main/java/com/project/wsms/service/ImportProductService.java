package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.ImportProduct;

public interface ImportProductService {
	
	public void save(ImportProduct ip_form);
	
	public void update(ImportProduct ip_form);

    public List<ImportProduct> getAll();
    
    public boolean existsById(String ip_formid);

    public void delete(String ip_formid);

	public Optional<ImportProduct> getById(String ip_formid);
}
