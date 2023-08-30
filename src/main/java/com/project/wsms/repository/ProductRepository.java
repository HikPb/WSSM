package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.wsms.model.Product;


public interface ProductRepository extends JpaRepository<Product, Integer>{
//	List<Product> findProductByName(String productName);
//	@Query(value="{'$or':[ {'productName' : {$regex : ?0}}, {'barcode': {$regex : ?0}}]}", fields="{ 'productName' : 1, 'barcode': 1}" )
    
	@Query(value = "select * from products p where concat(p.id, p.product_name, p.barcode) like %?1%", nativeQuery = true)
	List<Product> findByKeyword(String keyword);

	List<Product> findProductsByCategoriesId(Integer id);
	
    Page<Product> findByProductNameStartingWith(String productName, Pageable pageable);

	@Query(value = "SELECT * FROM products p WHERE LOWER(p.product_name) LIKE %?1% OR p.product_name LIKE %?1% " 
	+ "OR LOWER(p.barcode) LIKE %?1% OR  p.barcode LIKE %?1% OR CONCAT(p.id, '') LIKE %?1%", nativeQuery=true)
    Page<Product> searchBy(String search, Pageable pageable);
}
