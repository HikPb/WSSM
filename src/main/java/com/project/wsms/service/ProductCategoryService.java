package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.ProductCategory;

public interface ProductCategoryService{
	
	public List<ProductCategory> getAll();
	
	public List<ProductCategory> getByKeyword(String key);
	
	public ProductCategory save(ProductCategory category);

    public Optional<ProductCategory> getOne(String cateId);

    public void delete(String cateId);
}
