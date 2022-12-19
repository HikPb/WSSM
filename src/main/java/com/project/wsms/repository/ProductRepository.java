package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{
//	List<Product> findProductByName(String productName);
	@Query(value="{'$or':[ {'productName' : {$regex : ?0}}, {'barcode': {$regex : ?0}}]}", fields="{ 'productName' : 1, 'barcode': 1}" )
    List<Product> findByKeyword(String keyword);
    Page<Product> findByProductNameStartingWith(String productName, Pageable pageable);
}
