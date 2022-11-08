package com.project.wsms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.collection.Customer;
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

}
