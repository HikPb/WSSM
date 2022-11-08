package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.ProductCategory;
@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String>{

}
