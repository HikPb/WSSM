package com.project.wsms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.ui.Model;

import com.project.wsms.model.Supplier;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.SupplierService;

import jakarta.validation.Valid;

@Controller
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	

	@GetMapping("/supplier")
    public String view(Model model){
        model.addAttribute("pageTitle", "QUẢN LÝ NHÀ CUNG CẤP");
		return "supplier/supplier";
    }
	
	@GetMapping("/api/supplier")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllSupplier(){
		try{
			List<Supplier> listSup= supplierService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listSup), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query supplier data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}
	
	@GetMapping("/api/supplier/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> searchSupplier(@RequestParam("key") String key){
		try {
			List<Supplier> listSupplier = supplierService.getByKeyword(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query successfully", listSupplier), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}
	
	@GetMapping("/api/supplier/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Supplier> supplier = supplierService.getById(id);
		if (supplier.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", supplier.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/api/supplier")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveSupplier(@Valid @RequestBody Supplier supplier) {
		try {
			Supplier newSupplier = new Supplier();
			newSupplier.setSupName(supplier.getSupName());
			newSupplier.setSupPhone(supplier.getSupPhone());
			newSupplier.setAddress(supplier.getAddress()); 
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new supplier successfully", supplierService.save(newSupplier)), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new supplier", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PutMapping("/api/supplier/{id}")
	public ResponseEntity<ResponseObject> updateSupplier(@PathVariable Integer id, 
	                                        @RequestBody Supplier supplier) {
		
		Optional<Supplier> uSupplier = supplierService.getById(id);
		if(uSupplier.isPresent()){
			uSupplier.get().setSupName(supplier.getSupName());
			uSupplier.get().setSupPhone(supplier.getSupPhone());
			uSupplier.get().setAddress(supplier.getAddress());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update supplier successfully", supplierService.save(uSupplier.get())),
					HttpStatus.OK
					);
		
		}
		Supplier newSupplier = new Supplier();
		newSupplier.setSupName(supplier.getSupName());
		newSupplier.setSupPhone(supplier.getSupPhone());
		newSupplier.setAddress(supplier.getAddress());
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update supplier successfully", supplierService.save(newSupplier)),
				HttpStatus.OK
				);
	}
	
	@DeleteMapping("/api/supplier/{id}")
	public ResponseEntity<ResponseObject> deleteSupplier(@PathVariable(value = "id") Integer id) {
	    if(!supplierService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    supplierService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete supplier successfully", ""),
				HttpStatus.OK);
	}

}
