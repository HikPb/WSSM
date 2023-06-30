package com.project.wsms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.wsms.model.Order;
import com.project.wsms.payload.response.SbdResponse;
import com.project.wsms.payload.response.SbpResponse;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByCustomer_id(Integer id);

    @Query(value = "SELECT DATE_TRUNC('day', created_at) AS day,"
    + "SUM(profit) AS tprofit ,"
    + "SUM(revenue) AS trevenue ,"
    + "SUM(sales) AS tsales ,"
    + "COUNT(*) AS torder ,"
    + "SUM(total) AS tproduct ,"
    + "SUM(total_discount) AS tdiscount ,"
    + "SUM(shipping_fee) AS tshipfee ,"
    + "SUM(profit) AS tprofit "
    + "FROM orders o "
    + "WHERE DATE(o.created_at) >= :startDate "
    + "AND DATE(o.created_at) <= :endDate "
    + "GROUP BY DATE_TRUNC('day', created_at) "
    + "ORDER BY DATE_TRUNC('day', created_at) ASC"
    , nativeQuery = true)
    List<SbdResponse> getStatisticsByDay(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);

    @Query(value = "SELECT p.id, p.product_name AS productName, p.barcode,"
    + "COUNT(p.id) AS torder ,"
    + "SUM(oi.qty) AS tproduct ,"
    + "SUM(oi.discount) AS tdiscount ,"
    + "SUM(oi.qty * oi.price) AS tsales,"
    + "SUM(oi.qty * (oi.price - oi.discount)) AS trevenue ,"
    + "SUM(oi.qty * (oi.price - oi.discount - i.purcharse_price)) AS tprofit "
    + "FROM products p JOIN items i ON p.id = i.product_id "
    + "JOIN order_items oi ON oi.item_id = i.id "
    + "JOIN orders o ON o.id = oi.order_id "
    + "WHERE DATE(o.created_at) >= :startDate "
    + "AND DATE(o.created_at) <= :endDate "
    + "GROUP BY p.id "
    + "ORDER BY tproduct DESC"
    , nativeQuery = true)
    List<SbpResponse> getStatisticsByProduct(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);
}