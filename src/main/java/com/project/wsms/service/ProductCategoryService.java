package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.collection.ProductCategory;

public interface ProductCategoryService{
	
	public List<ProductCategory> getAll();
	
	public ProductCategory save(ProductCategory category);

    public Optional<ProductCategory> getOne(String cateId);

    public void delete(String cateId);
}
