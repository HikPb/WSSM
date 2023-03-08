package com.project.wsms.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.model.Employee;
import com.project.wsms.payload.request.UserRequest;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	

	@GetMapping("/users")
    public String view(Model model){
        model.addAttribute("pageTitle", "NHÂN VIÊN");
		return "users/employee";
    }

    @GetMapping("/profile")
	public String overview(Model model) {
		model.addAttribute("pageTitle", "HỒ SƠ");
		return "users/profile";
	}

    @GetMapping("/api/currentuser")
    @ResponseBody
    public ResponseEntity<ResponseObject> currentUserName(@CurrentSecurityContext(expression = "authentication") 
    Authentication authentication) {
        //System.out.println("xin" + authentication.getUsername());
        return  new ResponseEntity<>(
            new ResponseObject("ok", "mess",""), HttpStatus.OK );
    }

	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.toString();
    }
	@GetMapping("/api/employee")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllEmployee(){
		try{
			List<Employee> listSup= employeeService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listSup), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query employee data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}
	
	@GetMapping("/api/employee/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Employee> employee = employeeService.getById(id);
		if (employee.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", employee.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/api/employee")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveEmployee(@Valid @RequestBody UserRequest employee) {
		try {
			Employee newEmployee = new Employee();
			newEmployee.setUsername(employee.getUsername());
			newEmployee.setPassword(employee.getPassword());
            newEmployee.setRole(employee.getRole());
            employeeService.save(newEmployee);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new employee successfully", ""), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new employee", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PutMapping("/api/employee/{id}")
	public ResponseEntity<ResponseObject> updateEmployee(@PathVariable Integer id, 
	                                        @RequestBody Employee employee) {
		
		Optional<Employee> uEmployee = employeeService.getById(id);
		if(uEmployee.isPresent()){
			uEmployee.get().setFullname(employee.getFullname());
		    uEmployee.get().setPassword(employee.getPassword());
			uEmployee.get().setPhone(employee.getPhone());
            uEmployee.get().setRole(employee.getRole());
            employeeService.save(uEmployee.get());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update employee successfully", ""),
					HttpStatus.OK
					);
		
		}
		Employee newEmployee = new Employee();
		newEmployee.setUsername(employee.getUsername());
		newEmployee.setPassword(employee.getPassword());
        newEmployee.setRole(employee.getRole());
        employeeService.save(newEmployee);
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update employee successfully", ""),
				HttpStatus.OK
				);
	}
	
	@DeleteMapping("/api/employee/{id}")
	public ResponseEntity<ResponseObject> deleteEmployee(@PathVariable(value = "id") Integer id) {
	    if(!employeeService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    employeeService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete employee successfully", ""),
				HttpStatus.OK);
	}

}
