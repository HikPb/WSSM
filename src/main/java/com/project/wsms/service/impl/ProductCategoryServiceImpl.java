package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.collection.ProductCategory;
import com.project.wsms.repository.ProductCategoryRepository;
import com.project.wsms.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	private ProductCategoryRepository cateRepository;

	@Override
	public List<ProductCategory> getAll() {
		return cateRepository.findAll();
	}

	@Override
	public ProductCategory save(ProductCategory category) {
		cateRepository.save(category);
		return category;
	}

	@Override
	public Optional<ProductCategory> getOne(String cateId) {
		return cateRepository.findById(cateId);
	}

	@Override
	public void delete(String cateId) {
		cateRepository.deleteById(cateId);	
	}

	@Override
	public List<ProductCategory> getByKeyword(String key) {
		return cateRepository.findByKeyword(key);
	}
}
