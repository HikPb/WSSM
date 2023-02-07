package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Category;
import com.project.wsms.repository.CategoryRepository;
import com.project.wsms.service.CategoryService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository cateRepository;

	@Override
	public List<Category> getAll() {
		return cateRepository.findAll();
	}

	@Override
	public Category save(Category category) {
		cateRepository.save(category);
		return category;
	}

	@Override
	public Optional<Category> getById(Integer id) {
		return cateRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		cateRepository.deleteById(id);	
	}

	@Override
	public List<Category> getByKeyword(String key) {
		return cateRepository.findByCateNameContains(key);
	}

	@Override
	public boolean existsById(Integer id) {
		return cateRepository.existsById(id);
	}
}
