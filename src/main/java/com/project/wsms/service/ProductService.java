package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.wsms.model.Product;

public interface ProductService {

	public void save(Product product);
	
	public void update(Product product);
	
	public void updateIsSell(Integer productId, Boolean isSell);
	
	public boolean existsById(Integer productId);

    public List<Product> getAll();

    public void delete(Integer productId);

	public Page<Product> getAll(Pageable paging);
	
	public Page<Product> getByProductNameStartingWith(String productName, Pageable paging);
	
	public Optional<Product> getByProductId(Integer productId);
	
	public List<Product> getByProductKeyword(String keyword);

}
