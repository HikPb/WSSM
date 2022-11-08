package com.project.wsms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.wsms.repository.WarehouseRepository;
import com.project.wsms.service.WarehouseService;
@Service
public class WarehouseServiceImpl implements WarehouseService{

	@Autowired
	private WarehouseRepository wareRepository;
}
