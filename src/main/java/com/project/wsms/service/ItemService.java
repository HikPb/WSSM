package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Item;

public interface ItemService {

	public List<Item> getAll();
	
	public List<Item> getByKeyword(String key);
	
	public boolean existsById(Integer id);
	
	public Item save(Item item);

    public Optional<Item> getById(Integer id);

    public void delete(Integer id);
}
