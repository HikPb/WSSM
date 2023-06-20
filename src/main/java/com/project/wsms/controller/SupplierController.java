package com.project.wsms.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Supplier;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.SupplierService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;

	@Autowired
	private EmployeeService employeeService;
	

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/supplier")
    public String view(Model model, HttpServletRequest request){
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
        model.addAttribute("pageTitle", "QUẢN LÝ NHÀ CUNG CẤP");
		return "supplier/supplier";
    }

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
