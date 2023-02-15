package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Export;
import com.project.wsms.repository.ExportRepository;
import com.project.wsms.service.ExportService;

@Service
public class ExportServiceImpl implements ExportService{
	
	@Autowired
	private ExportRepository ipRepository;

	@Override
	public void save(Export epform) {
		ipRepository.save(epform);
		
	}

	@Override
	public List<Export> getAll() {
		return ipRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return ipRepository.existsById(id);
	}

	@Override
	public void delete(Integer id) {
		ipRepository.deleteById(id);
		
	}

	@Override
	public Optional<Export> getById(Integer id) {
		return ipRepository.findById(id);
	}
	
	

}
