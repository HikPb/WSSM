package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.ProductCategory;
@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String>{

	@Query(value="{'cateName' : {$regex : ?0}}", fields="{ 'cateName' : 1}" )
    List<ProductCategory> findByKeyword(String keyword);
}
