package com.project.wsms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
