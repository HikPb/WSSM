package com.project.wsms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.wsms.collection.Product;
import com.project.wsms.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
//	@GetMapping("/products")
//	public List<Product> findProducts(){
//		return productService
//	}
//	
//	@GetMapping("/products/{productId}")
//	public Product findProduct() {
//		
//	}
	
}
