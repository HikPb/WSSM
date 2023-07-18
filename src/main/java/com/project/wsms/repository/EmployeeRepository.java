package com.project.wsms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.wsms.model.ERole;
import com.project.wsms.model.Employee;
import com.project.wsms.payload.response.SbeResponse;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByUsername(String username);
    List<Employee> findEmployeesByRolesName(ERole roleName);
    Boolean existsByUsername(String username);

    @Query(value = "select viewaa.id, viewaa.username, viewaa.torder, viewa.torder as torder1, viewa.tproduct, viewa.tdiscount, viewa.tshipfee, viewa.tsales, viewa.trevenue, viewa.tprofit, \n" + //
            "viewb.torder as torder2, viewb.tproduct as tproduct2, viewb.tdiscount as tdiscount2, viewb.tshipfee as tshipfee2, viewb.tsales as tsales2, viewb.trevenue as trevenue2, viewb.tprofit as tprofit2,\n" + //
            "viewc.torder as torder3, viewd.torder as torder4 from \n" + //
            "(select e.username, e.id, count(e.id) as torder\n" + //
            "from employee e join orders o on e.id = o.emp_id\n" + //
            "where o.status in (1,2,3,4,5,6,7) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate \n" + //
            "group by e.id) viewaa\n" + //
            "full join (select e.username, e.id, count(e.id) as torder, sum(total) as tproduct, sum(total_discount) as tdiscount, sum(shipping_fee) as tshipfee, sum(sales) as tsales, sum(revenue) as trevenue , sum(profit) as tprofit \n" + //
            "from employee e join orders o on e.id = o.emp_id\n" + //
            "where o.status in (5) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate\n" + //
            "group by e.id) viewa on viewaa.id = viewa.id\n" + //
            "full join (select e.id, count(e.id) as torder, sum(total) as tproduct, sum(total_discount) as tdiscount, sum(shipping_fee) as tshipfee, sum(sales) as tsales, sum(revenue) as trevenue , sum(profit) as tprofit \n" + //
            "from employee e join orders o on e.id = o.emp_id\n" + //
            "where o.status in (1,2,3,4) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate\n" + //
            "group by e.id) viewb on viewaa.id = viewb.id\n" + //
            "full join (select e.id, count(e.id) as torder\n" + //
            "from employee e join orders o on e.id = o.emp_id\n" + //
            "where o.status in (6,7) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate\n" + //
            "group by e.id) viewc on viewaa.id = viewc.id\n" + //
            "full join (select e.id, count(e.id) as torder\n" + //
            "from employee e join orders o on e.id = o.emp_id\n" + //
            "where o.status in (0) and DATE(o.created_at) >= :startDate and DATE(o.created_at) <= :endDate\n" + //
            "group by e.id) viewd on viewaa.id = viewd.id"
    , nativeQuery = true)
    List<SbeResponse> getStatisticsByEmployee(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);

    @Query(value = "SELECT e.id , e.username ,"
    + "SUM(profit) AS tprofit ,"
    + "SUM(revenue) AS trevenue ,"
    + "SUM(sales) AS tsales ,"
    + "COUNT(e.id) AS torder ,"
    + "SUM(total) AS tproduct ,"
    + "SUM(total_discount) AS tdiscount ,"
    + "SUM(shipping_fee) AS tshipfee "
    + "FROM employee e join orders o on e.id = o.emp_id "
    + "WHERE o.status IN (1,2,3,4,5) AND DATE(o.created_at) >= :startDate "
    + "AND DATE(o.created_at) <= :endDate "
    + "GROUP BY e.id "
    + "ORDER BY torder DESC"
    , nativeQuery = true)
    List<SbeResponse> getStatisticsByEmployee2(
        @Param("startDate") Date startDate, 
        @Param("endDate") Date endDate);
}
