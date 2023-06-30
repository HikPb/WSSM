package com.project.wsms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.wsms.model.Employee;
import com.project.wsms.payload.response.SbeResponse;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query(value = "SELECT e.id , e.username ,"
    + "SUM(profit) AS tprofit ,"
    + "SUM(revenue) AS trevenue ,"
    + "SUM(sales) AS tsales ,"
    + "COUNT(e.id) AS torder ,"
    + "SUM(total) AS tproduct ,"
    + "SUM(total_discount) AS tdiscount ,"
    + "SUM(shipping_fee) AS tshipfee "
    + "FROM employee e join orders o on e.id = o.emp_id "
    + "WHERE DATE(o.created_at) >= :startDate "
    + "AND DATE(o.created_at) <= :endDate "
    + "GROUP BY e.id "
    + "ORDER BY torder DESC"
    , nativeQuery = true)
    List<SbeResponse> getStatisticsByEmployee(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);
}
