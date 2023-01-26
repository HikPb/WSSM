package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.wsms.model.Customer;
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
//	List<Customer> findCustomerByName(String name);
//
//    //List<Customer> findByAgeBetween(Integer min, Integer max);
//
////    @Query(value = "{ 'age' : { $gt : ?0, $lt : ?1}}",
////           fields = "{addresses:  0}")
//    List<Customer> findCustomerByPhone(String phone);
}
