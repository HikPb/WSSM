package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
//	List<Customer> findCustomerByName(String name);
//
//    //List<Customer> findByAgeBetween(Integer min, Integer max);
//
////    @Query(value = "{ 'age' : { $gt : ?0, $lt : ?1}}",
////           fields = "{addresses:  0}")
//    List<Customer> findCustomerByPhone(String phone);
	List<Customer> findByNameContains(String name);
	List<Customer> findByPhoneContains(String phone);
}
