package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.wsms.collection.Product;

public interface ProductService {

	public void save(Product product);

    public List<Product> getAll();

    public void delete(String productId);

//    public List<Product> getProductByBarcode(String barcode);

	public Page<Product> getAll(Pageable paging);

//	public List<Product> getProductByName(String productName);
	
	public Page<Product> getByProductNameStartingWith(String productName, Pageable paging);
	
	public Optional<Product> getByProductId(String productId);

}
