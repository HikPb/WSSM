package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

}
