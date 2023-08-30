package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Product;
import com.project.wsms.repository.CategoryRepository;
import com.project.wsms.repository.ProductRepository;
import com.project.wsms.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired CategoryRepository categoryRepository;

	@Transactional
	@Override
	public void save(Product product) {
		productRepository.save(product);
	}

	@Override
	public void delete(Integer id) {
		productRepository.deleteById(id);
		
	}

	@Override
	public List<Product> getAll() {
		return productRepository.findAll();
	}

	@Override
	public Page<Product> getAll(Pageable paging) {
		return productRepository.findAll(paging);
	}

	@Override
	public Page<Product> getByProductNameStartingWith(String productName, Pageable paging) {
		
		return productRepository.findByProductNameStartingWith(productName, paging);
	}

	@Override
	public Optional<Product> getByProductId(Integer productId) {
		return productRepository.findById(productId);
	}

	@Override
	public void update(Product product) {
		if(productRepository.existsById(product.getId())) {
			productRepository.save(product);
		}
		
	}

	@Override
	public void updateIsSell(Integer productId, Boolean isSell) {
		Product product = productRepository.findById(productId).get();
		//product.setIsSell(isSell);
		productRepository.save(product);
	}

	@Override
	public List<Product> getByProductKeyword(String keyword) {
		
		return productRepository.findByKeyword(keyword);
	}

	@Override
	public boolean existsById(Integer productId) {
		return productRepository.existsById(productId);
	}

	@Override
	public Page<Product> searchProducts(String key, Pageable paging) {
		return productRepository.searchBy(key, paging);
	}

	@Override
	public List<Product> getByCategoryId(Integer id) {
		return productRepository.findProductsByCategoriesId(id);
	}
}
