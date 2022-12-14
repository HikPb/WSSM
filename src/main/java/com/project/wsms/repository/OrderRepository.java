package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}
