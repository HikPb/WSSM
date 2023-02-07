package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Category;

public interface CategoryService{
	
	public List<Category> getAll();
	
	public List<Category> getByKeyword(String key);
	
	public boolean existsById(Integer id);
	
	public Category save(Category category);

    public Optional<Category> getById(Integer id);

    public void delete(Integer id);
}
