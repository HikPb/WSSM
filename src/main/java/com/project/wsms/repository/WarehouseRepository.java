package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.Warehouse;
@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, String> {

}
