package com.project.wsms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.wsms.model.Warehouse;
import com.project.wsms.payload.response.SbwResponse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
	List<Warehouse> findByNameContains(String name);

	@Query(value = "SELECT w.id , w.name ,"
    + "SUM(profit) AS tprofit ,"
    + "SUM(revenue) AS trevenue ,"
    + "SUM(sales) AS tsales ,"
    + "COUNT(w.id) AS torder ,"
    + "SUM(total) AS tproduct ,"
    + "SUM(total_discount) AS tdiscount ,"
    + "SUM(shipping_fee) AS tshipfee "
    + "FROM warehouses w join orders o on w.id = o.ware_id "
	+ "WHERE o.status IN (1,2,3,4,5) AND DATE(o.created_at) >= :startDate "
    + "AND DATE(o.created_at) <= :endDate "
    + "GROUP BY w.id "
	+ "ORDER BY torder DESC"
    , nativeQuery = true)
    List<SbwResponse> getStatisticsByWarehouse(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);
}
