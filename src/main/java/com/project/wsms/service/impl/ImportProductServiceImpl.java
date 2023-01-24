package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.collection.ImportProduct;
import com.project.wsms.collection.Order;
import com.project.wsms.repository.ImportProductRepository;
import com.project.wsms.service.ImportProductService;

@Service
public class ImportProductServiceImpl implements ImportProductService{
	
	@Autowired
	private ImportProductRepository ipRepository;

	@Override
	public void save(ImportProduct ip_form) {
		ipRepository.save(ip_form);
		
	}

	@Override
	public void update(ImportProduct ip_form) {
		ipRepository.save(ip_form);
		
	}

	@Override
	public List<ImportProduct> getAll() {
		return ipRepository.findAll();
	}
	
	public boolean existsById(String ip_formid) {
		return ipRepository.existsById(ip_formid);
	}

	@Override
	public void delete(String ip_formid) {
		ipRepository.deleteById(ip_formid);
		
	}

	@Override
	public Optional<ImportProduct> getById(String ip_formid) {
		return ipRepository.findById(ip_formid);
	}
	
	

}
