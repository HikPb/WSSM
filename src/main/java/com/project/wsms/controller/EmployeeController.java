package com.project.wsms.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.model.Employee;
import com.project.wsms.payload.request.ChangePwRequest;
import com.project.wsms.payload.request.EmployeeRequest;
import com.project.wsms.payload.request.PwRequest;
import com.project.wsms.payload.request.UserRequest;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
    private PasswordEncoder passwordEncoder;
	

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
    public String view(Model model, HttpServletRequest request){
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
        model.addAttribute("pageTitle", "NHÂN VIÊN");
		return "users/employee";
    }

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

    @GetMapping("/profile")
	public String overview(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "HỒ SƠ");
		return "users/profile";
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

    @GetMapping("/api/currentuser")
    @ResponseBody
    public ResponseEntity<ResponseObject> currentUserName(@CurrentSecurityContext(expression = "authentication") 
    Authentication authentication) {
        //System.out.println("xin" + authentication.getUsername());
        return  new ResponseEntity<>(
            new ResponseObject("ok", "mess",""), HttpStatus.OK );
    }

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.toString();
    }

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@GetMapping("/api/employee")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllEmployee(){
		try{
			List<Employee> listSup= employeeService.getAll();
			List<EmployeeDto> listEmpDto = listSup.stream().map( emp -> {
				EmployeeDto empDto = new EmployeeDto();
				empDto.convertToDto(emp);
				return empDto;
			}).collect(Collectors.toList());
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listEmpDto), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query employee data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@GetMapping("/api/employee/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Employee> employee = employeeService.getById(id);
		if (employee.isPresent()) {
			EmployeeDto employeeDto = new EmployeeDto();
			employeeDto.convertToDto(employee.get());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", employeeDto),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/api/employee")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveEmployee(@Valid @RequestBody UserRequest employee) {
		try {
			Optional<Employee> emp = employeeService.getByUsername(employee.getUsername());
			if(emp.isPresent()){
				return new ResponseEntity<>(
					new ResponseObject("ok", "This username is already in use", ""), 
					HttpStatus.OK
					);
			}
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
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@PutMapping("/api/employee/{id}")
	public ResponseEntity<ResponseObject> updateEmployee(@PathVariable Integer id, 
	                                        @RequestBody EmployeeRequest employee) {
		
		Optional<Employee> uEmployee = employeeService.getById(id);
		if(uEmployee.isPresent()){
			uEmployee.get().setFullname(employee.getFullname());
			uEmployee.get().setPhone(employee.getPhone());
            employeeService.save(uEmployee.get());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update employee successfully", ""),
					HttpStatus.OK
					);
		
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Employee not found", ""),
				HttpStatus.BAD_REQUEST
				);
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

	@PutMapping("/api/employee/{id}/changepassword")
	public ResponseEntity<ResponseObject> changePassword(@PathVariable(value = "id") Integer id, @RequestBody ChangePwRequest cqRequest, HttpServletRequest request) {
	    Optional<Employee> uEmployee = employeeService.getById(id);
		if(uEmployee.isPresent()){
			Principal principal = request.getUserPrincipal();
			if(uEmployee.get().getUsername().compareTo(principal.getName())==0){
				if(passwordEncoder.matches(cqRequest.getPassword(), uEmployee.get().getPassword())){
					uEmployee.get().setPassword(cqRequest.getNewPassword());
					employeeService.save(uEmployee.get());
					return new ResponseEntity<>(
						new ResponseObject("ok", "Change password successfully", ""),
						HttpStatus.OK
						);
				}else{
					return new ResponseEntity<>(
						new ResponseObject("failed", "Password incorrect", ""),
						HttpStatus.OK
						);
				}
			}
			return new ResponseEntity<>(
				new ResponseObject("failed", "Asynchronously edited object", ""),
				HttpStatus.BAD_REQUEST);
		}
	    return new ResponseEntity<>(
				new ResponseObject("failed", "Employee not found", ""),
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/api/employee/admin-change")
	public ResponseEntity<ResponseObject> changePasswordByAdmin(@RequestBody PwRequest request) {
	    Optional<Employee> uEmployee = employeeService.getById(request.getId());
		if(uEmployee.isPresent()){
			if(request.getNewPassword()!= null && request.getNewPassword() != "") {
				uEmployee.get().setPassword(request.getNewPassword());
			}
			uEmployee.get().setRole(request.getRole());
			employeeService.save(uEmployee.get());
			return new ResponseEntity<>(
				new ResponseObject("ok", "Update employee password successfully", ""),
				HttpStatus.OK);
		}
	    return new ResponseEntity<>(
				new ResponseObject("failed", "Employee not found", ""),
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('ADMIN')")
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
