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
    List<Order> findByStatusIn(List<Integer> listStatus);

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
    List<SbdResponse> getStatisticsByDay2(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);

    @Query(value = "SELECT viewa.date AS day, viewa.count AS torder, "
    + "viewb.count AS torder1, viewb.tdiscount, viewb.tshipfee, viewb.tsales, viewb.trevenue, viewb.tprofit, viewb.tproduct, " 
    + "viewc.count AS torder2, viewc.tdiscount AS tdiscount2, viewc.tshipfee AS tshipfee2, viewc.tsales AS tsale2, viewc.trevenue AS trevenue2, viewc.tprofit AS tprofit2, viewc.tproduct AS tproduct2, "
    + "viewd.count AS torder3, viewe.count AS torder4 "
    + "FROM "
    + "(SELECT distinct DATE_TRUNC('day', created_at) AS date, COUNT(*) FROM orders o "
    + "WHERE o.status in (1,2,3,4,5,6,7) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate "
    + "GROUP BY DATE_TRUNC('day', created_at)) viewa "
    + "FULL JOIN "
    + "(SELECT DATE_TRUNC('day', created_at) AS date, COUNT(*), SUM(total_discount) AS tdiscount, SUM(shipping_fee) AS tshipfee, SUM(sales) AS tsales, SUM(total) AS tproduct, SUM(revenue) AS trevenue , SUM(profit) AS tprofit FROM orders o "
    + "WHERE o.status in (5) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate "
    + "GROUP BY DATE_TRUNC('day', created_at)) viewb on viewa.date = viewb.date " 
    + "FULL JOIN "
    + "(SELECT DATE_TRUNC('day', created_at) AS date, COUNT(*), SUM(total_discount) AS tdiscount, SUM(shipping_fee) AS tshipfee, SUM(sales) AS tsales, SUM(total) AS tproduct, SUM(revenue) AS trevenue , SUM(profit) AS tprofit FROM orders o "
    + "WHERE o.status in (1,2,3,4) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate "
    + "GROUP BY DATE_TRUNC('day', created_at)) viewc on viewa.date = viewc.date "
    + "FULL JOIN "
    + "(SELECT DATE_TRUNC('day', created_at) AS date, COUNT(*) FROM orders o "
    + "WHERE o.status in (6,7) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate "
    + "GROUP BY DATE_TRUNC('day', created_at)) viewd on viewa.date = viewd.date "
    + "FULL JOIN"
    + "(SELECT DATE_TRUNC('day', created_at) AS date, COUNT(*) FROM orders o "
    + "WHERE o.status = 0 and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate "
    + "GROUP BY DATE_TRUNC('day', created_at)) viewe on viewa.date = viewe.date "
    + "ORDER BY viewa.date asc", nativeQuery = true)
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
    + "WHERE o.status IN (1,2,3,4,5) AND DATE(o.created_at) >= :startDate "
    + "AND DATE(o.created_at) <= :endDate "
    + "GROUP BY p.id "
    + "ORDER BY tproduct DESC"
    , nativeQuery = true)
    List<SbpResponse> getStatisticsByProduct(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);
}