package com.project.wsms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.repository.ProductSupplierRepository;
import com.project.wsms.service.ProductCategoryService;

@Service
public class ProductSupplierServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductSupplierRepository supplierRepository;
	

}
