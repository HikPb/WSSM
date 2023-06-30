package com.project.wsms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.wsms.model.Item;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("/item")
	public ResponseEntity<ResponseObject> listAllItem(){
		List<Item> listItem= itemService.getAll();
		
		if(listItem.isEmpty()) { 
			return new ResponseEntity<>( 
				new ResponseObject("empty", "No content", ""), HttpStatus.NO_CONTENT); 
			}
		
		return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listItem), 
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("item/search")
	public ResponseEntity<ResponseObject> searchItem(@RequestParam("key") String key){
		try {
			List<Item> listItem = itemService.getBySku(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query categories successfully", listItem), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("/item/{id}")
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Item> item = itemService.getById(id);
		if (item.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", item.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find item with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("/item/warehouse/{id}")
	public ResponseEntity<ResponseObject> getAllInWarehouse(@PathVariable("id") Integer id) {
		try{
			List<Item> item = itemService.getByWareId(id);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query item successfully", item),
					HttpStatus.OK);		
		} catch (Exception e){
			return new ResponseEntity<>(
				new ResponseObject("Failed", "Error when searching with id = ", ""),
				HttpStatus.BAD_REQUEST);
		}		
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("/item/product/{id}/warehouse/{wid}")
	public ResponseEntity<ResponseObject> getByProductAndWarehouse(@PathVariable("id") Integer id, @PathVariable("wid") Integer wid) {
		try {
			Item item = itemService.getByWareIdProductId(wid, id);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query item successfully", item),
					HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity<>(
				new ResponseObject("Failed", "Error when searching item with product id = " + id, ""),
				HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("/item/product/{id}")
	public ResponseEntity<ResponseObject> getAllInProducts(@PathVariable("id") Integer id) {
		try {
			List<Item> item = itemService.getByProductId(id);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query item successfully", item),
					HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity<>(
				new ResponseObject("Failed", "Error when searching with id = " + id, ""),
				HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@PostMapping("/item/{id}/status")
	public ResponseEntity<ResponseObject> changeItemStatus(@PathVariable("id") Integer id) {
		try {
			Optional<Item> item = itemService.getById(id);
			if(item.isPresent()){
				if(item.get().isActive()){
					item.get().setActive(false);
				}else{
					item.get().setActive(true);
				}
				itemService.save(item.get());
			}
			return new ResponseEntity<>(
					new ResponseObject("ok", "Change item status successfully", item.get()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
				new ResponseObject("false", "Problem when changing item status", ""),
				HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@PostMapping("/api/item")
	public ResponseEntity<ResponseObject> saveItem(@Valid @RequestBody Item item) {
		try {
			Item newItem = new Item();
			newItem.setActive(item.isActive());
			/*
			 * newItem.set(item.getSupPhone()); newItem.setAddress(item.getAddress());
			 */ 
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new item successfully", itemService.save(newItem)), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new item", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@PutMapping("/api/item/{id}")
	public ResponseEntity<ResponseObject> updateItem(@PathVariable Integer id, 
	                                        @RequestBody Item item) {
		
		Optional<Item> uItem = itemService.getById(id);
		if(uItem.isPresent()){
			//uItem.get().setSupName(item.getSupName());
			//uItem.get().setSupPhone(item.getSupPhone());
			//uItem.get().setAddress(item.getAddress());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update item successfully", itemService.save(uItem.get())),
					HttpStatus.OK
					);
		
		}
		Item newItem = new Item();
		//newItem.setSupName(item.getSupName());
		//newItem.setSupPhone(item.getSupPhone());
		//newItem.setAddress(item.getAddress());
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update item successfully", itemService.save(newItem)),
				HttpStatus.OK
				);
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@DeleteMapping("/api/item/{id}")
	public ResponseEntity<ResponseObject> deleteItem(@PathVariable(value = "id") Integer id) {
	    if(!itemService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    itemService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete item successfully", ""),
				HttpStatus.OK);
	}

}
