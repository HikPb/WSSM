package com.project.wsms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Import;
import com.project.wsms.repository.ImportRepository;
import com.project.wsms.service.ImportService;

@Service
public class ImportServiceImpl implements ImportService{
	
	@Autowired
	private ImportRepository ipRepository;

	@Override
	public void save(Import ip_form) {
		ipRepository.save(ip_form);
		
	}

	@Override
	public void update(Import ip_form) {
		ipRepository.save(ip_form);
		
	}

	@Override
	public List<Import> getAll() {
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
	public Optional<Import> getById(Integer id) {
		return ipRepository.findById(id);
	}
	
	

}
