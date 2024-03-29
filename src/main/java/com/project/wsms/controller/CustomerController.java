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
import com.project.wsms.model.Customer;
import com.project.wsms.model.Employee;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.CustomerService;
import com.project.wsms.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class CustomerController {

    @Autowired
	private CustomerService customerService;
	
	@Autowired
	private EmployeeService employeeService;

	@PreAuthorize("hasRole('SALES_EMPLOYEE')")

	@GetMapping("/customer")
    public String view(Model model, HttpServletRequest request){
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
        model.addAttribute("pageTitle", "QUẢN LÝ KHÁCH HÀNG");
		return "customer/customer";
    }
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/customer")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllCustomer(){
		List<Customer> listCustomer= customerService.getAll();
		
		if(listCustomer.isEmpty()) { 
			return new ResponseEntity<>( 
				new ResponseObject("empty", "No content", ""), HttpStatus.OK); 
			}
		
		return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listCustomer), 
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/customer/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> searchCustomerByPhone(@RequestParam("phone") String key){
		try {
			List<Customer> listCustomer = customerService.getByPhone(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query customer successfully", listCustomer), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/customer/searchname")
	@ResponseBody
	public ResponseEntity<ResponseObject> searchCustomerByName(@RequestParam("name") String key){
		try {
			List<Customer> listCustomer = customerService.getByName(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query customer successfully", listCustomer), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/customer/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Customer> customer = customerService.getById(id);
		if (customer.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", customer.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE')")

	@PostMapping("/api/customer")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveCustomer(@Valid @RequestBody Customer customer) {
		try {
			Customer newCustomer = new Customer();
			newCustomer.setName(customer.getName());
			newCustomer.setPhone(customer.getPhone());
			newCustomer.setDob(customer.getDob()); 
			newCustomer.setAddress(customer.getAddress()); 
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new customer successfully", customerService.save(newCustomer)), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new customer", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE')")

	@PutMapping("/api/customer/{id}")
	public ResponseEntity<ResponseObject> updateCustomer(@PathVariable Integer id, 
	                                        @RequestBody Customer customer) {
		
		Optional<Customer> uCustomer = customerService.getById(id);
		if(uCustomer.isPresent()){
			uCustomer.get().setName(customer.getName());
			uCustomer.get().setPhone(customer.getPhone());
			uCustomer.get().setDob(customer.getDob()); 
			uCustomer.get().setAddress(customer.getAddress());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update customer successfully", customerService.save(uCustomer.get())),
					HttpStatus.OK
					);
		
		}
		Customer newCustomer = new Customer();
		newCustomer.setName(customer.getName());
		newCustomer.setPhone(customer.getPhone());
		newCustomer.setDob(customer.getDob()); 
		newCustomer.setAddress(customer.getAddress());
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update customer successfully", customerService.save(newCustomer)),
				HttpStatus.OK
				);
	}
	
	@PreAuthorize("hasRole('SALES_ADMIN')")

	@DeleteMapping("/api/customer/{id}")
	public ResponseEntity<ResponseObject> deleteCustomer(@PathVariable(value = "id") Integer id) {
	    if(!customerService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    customerService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete customer successfully", ""),
				HttpStatus.OK);
	}
}
