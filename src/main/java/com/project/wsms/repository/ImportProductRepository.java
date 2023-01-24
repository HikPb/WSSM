package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.wsms.collection.ImportProduct;

public interface ImportProductRepository extends MongoRepository<ImportProduct, String>{

}
