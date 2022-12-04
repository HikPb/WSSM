package com.project.wsms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{
//	List<Product> findProductByName(String productName);
//    List<Product> findProductByBarcode(String barcode);
    Page<Product> findByProductNameStartingWith(String productName, Pageable pageable);
}
