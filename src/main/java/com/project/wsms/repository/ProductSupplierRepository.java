package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.ProductSupplier;

@Repository
public interface ProductSupplierRepository extends MongoRepository<ProductSupplier, String> {

}
