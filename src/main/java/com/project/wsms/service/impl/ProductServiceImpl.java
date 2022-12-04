package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.wsms.collection.Product;
import com.project.wsms.repository.ProductRepository;
import com.project.wsms.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void save(Product product) {
		// TODO Auto-generated method stub
		productRepository.save(product);
	}

//	@Override
//	public List<Product> getProductByName(String productName) {
//		// TODO Auto-generated method stub
//		return productRepository.findProductByName(productName);
//	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		productRepository.deleteById(id);
		
	}

//	@Override
//	public List<Product> getProductByBarcode(String barcode) {
//		// TODO Auto-generated method stub
//		return productRepository.findProductByBarcode(barcode);
//	}

	@Override
	public List<Product> getAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public Page<Product> getAll(Pageable paging) {
		// TODO Auto-generated method stub
		return productRepository.findAll(paging);
	}

	@Override
	public Page<Product> getByProductNameStartingWith(String productName, Pageable paging) {
		
		return productRepository.findByProductNameStartingWith(productName, paging);
	}

	@Override
	public Optional<Product> getByProductId(String productId) {
		// TODO Auto-generated method stub
		return productRepository.findById(productId);
	}
	

}
