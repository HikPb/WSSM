package com.project.wsms.service;

import java.util.List;
import java.util.Optional;

import com.project.wsms.model.Item;

public interface ItemService {

	public List<Item> getAll();
	
	public List<Item> getByWareId(Integer wareId);

	public List<Item> getByProductId(Integer wareId);
	
	public List<Item> getBySku(String key);
	
	public boolean existsById(Integer id);
	
	public Item save(Item item);

    public Optional<Item> getById(Integer id);

	public Item getByWareIdProductId(Integer wareId, Integer productId);

    public void delete(Integer id);
}
