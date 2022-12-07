package com.project.wsms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.repository.ProductSupplierRepository;
import com.project.wsms.service.ProductSupplierService;

@Service
public class ProductSupplierServiceImpl implements ProductSupplierService {
	
	@Autowired
	private ProductSupplierRepository supplierRepository;
	

}
