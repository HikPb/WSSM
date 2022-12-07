package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.wsms.collection.Product;

public interface ProductService {

	public void save(Product product);
	
	public void update(Product product);

    public List<Product> getAll();

    public void delete(String productId);

	public Page<Product> getAll(Pageable paging);
	
	public Page<Product> getByProductNameStartingWith(String productName, Pageable paging);
	
	public Optional<Product> getByProductId(String productId);

}
