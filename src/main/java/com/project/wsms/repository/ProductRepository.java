package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.wsms.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
//	List<Product> findProductByName(String productName);
//	@Query(value="{'$or':[ {'productName' : {$regex : ?0}}, {'barcode': {$regex : ?0}}]}", fields="{ 'productName' : 1, 'barcode': 1}" )
    
	@Query(value = "select * from products p where concat(p.id, p.product_name, p.barcode) like %?1%", nativeQuery = true)
	List<Product> findByKeyword(String keyword);
    
    Page<Product> findByProductNameStartingWith(String productName, Pageable pageable);
}
