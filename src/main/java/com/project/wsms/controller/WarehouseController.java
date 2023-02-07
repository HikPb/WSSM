package com.project.wsms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.model.ResponseObject;
import com.project.wsms.model.Warehouse;
import com.project.wsms.service.WarehouseService;

import jakarta.validation.Valid;

@Controller
public class WarehouseController {

	@Autowired
	private WarehouseService warehouseService;
	
	@GetMapping("/warehouse")
    public String view(Model model){
        model.addAttribute("pageTitle", "QUẢN LÝ KHO HÀNG");
		return "warehouse";
    }
    
	// VIEW KIỂM KHO
	@GetMapping("checkquantity")
	public String getAllCheckQuantity(Model model) {
		// model.addAttribute("products", productService.getAll());
		model.addAttribute("pageTitle", "KIỂM KHO");
		return "warehouse/check-quantity";
	}

	// VIEW XUẤT KHO

	@GetMapping("/export")
	public String getAllExportProduct(Model model) {
		// model.addAttribute("products", productService.getAll());
		model.addAttribute("pageTitle", "XUẤT KHO");
		return "warehouse/export";
	}
	
	@GetMapping("/api/warehouse")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllWarehouse(){
		List<Warehouse> listWarehouse= warehouseService.getAll();
		if(listWarehouse.isEmpty()) {
			return new ResponseEntity<>(
					new ResponseObject("empty", "No content", ""),
					HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listWarehouse), 
				HttpStatus.OK);
	}
	
	@GetMapping("/api/warehouse/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> searchWarehouse(@RequestParam("key") String key){
		try {
			List<Warehouse> listWarehouse = warehouseService.getByKeyword(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query categories successfully", listWarehouse), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}
	
	@GetMapping("/api/warehouse/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Warehouse> warehouse = warehouseService.getById(id);
		if (warehouse.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", warehouse.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/api/warehouse/")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveWarehouse(@Valid @RequestBody Warehouse warehouse) {
		try {
			Warehouse newWarehouse = new Warehouse();
			newWarehouse.setName(warehouse.getName());
			newWarehouse.setPhone(warehouse.getPhone());
			//TODO newWarehouse.setWareAddress(null);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new warehouse successfully", warehouseService.save(newWarehouse)), 
					HttpStatus.BAD_REQUEST
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new warehouse", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PutMapping("/api/warehouse/{id}")
	public ResponseEntity<ResponseObject> updateWarehouse(@PathVariable Integer id, 
	                                        @RequestBody Warehouse warehouse) {
		
		Optional<Warehouse> uWarehouse = warehouseService.getById(id);
		if(uWarehouse.isPresent()){
					
			uWarehouse.get().setName(warehouse.getName());
			uWarehouse.get().setPhone(warehouse.getPhone());
			//TODO address
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update warehouse successfully", warehouseService.save(uWarehouse.get())),
					HttpStatus.OK
					);
		
		}
		Warehouse newWarehouse = new Warehouse();
		newWarehouse.setName(warehouse.getName());
		newWarehouse.setPhone(warehouse.getPhone());
		//TODO address
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update warehouse successfully", warehouseService.save(newWarehouse)),
				HttpStatus.OK
				);
	}
	
	@DeleteMapping("/api/warehouse/{id}")
	public ResponseEntity<ResponseObject> deleteWarehouse(@PathVariable(value = "id") Integer id) {
	    if(!warehouseService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    warehouseService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete warehouse successfully", ""),
				HttpStatus.OK);
	}
}
