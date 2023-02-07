package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Import;

public interface ImportService {
	
	public void save(Import ip_form);
	
	public void update(Import ip_form);

    public List<Import> getAll();
    
    public boolean existsById(Integer id);

    public void delete(Integer id);

	public Optional<Import> getById(Integer id);
}
