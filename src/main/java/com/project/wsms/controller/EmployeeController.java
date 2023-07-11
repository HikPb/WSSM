package com.project.wsms.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.project.wsms.model.ERole;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Role;
import com.project.wsms.payload.request.AdChangePwRequest;
import com.project.wsms.payload.request.AdChangeRoleRequest;
import com.project.wsms.payload.request.ChangePwRequest;
import com.project.wsms.payload.request.EmployeeRequest;
import com.project.wsms.payload.request.UserRequest;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.repository.RoleRepository;
import com.project.wsms.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	

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

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")

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

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")

    @GetMapping("/api/currentuser")
    @ResponseBody
    public ResponseEntity<ResponseObject> currentUserName(@CurrentSecurityContext(expression = "authentication") 
    Authentication authentication) {
        //System.out.println("xin" + authentication.getUsername());
        return  new ResponseEntity<>(
            new ResponseObject("ok", "mess",""), HttpStatus.OK );
    }

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")

	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.toString();
    }

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")

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
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
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
	public ResponseEntity<ResponseObject> createNewEmployee(@Valid @RequestBody UserRequest employee) {
		try {
			if(employeeService.existsByUsername(employee.getUsername())){
				return new ResponseEntity<>(
					new ResponseObject("failed", "This username is already in use", ""), 
					HttpStatus.OK
					);
			}
			Employee newEmployee = new Employee();
			newEmployee.setUsername(employee.getUsername());
			newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
			Set<Role> roles = new HashSet<>();
			Role userRole = roleRepository.findByName(ERole.ROLE_SALES_EMPLOYEE)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
			newEmployee.setRoles(roles);
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
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
	@ResponseBody
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

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
	@ResponseBody
	@PutMapping("/api/employee/{id}/changepassword")
	public ResponseEntity<ResponseObject> changePassword(@PathVariable(value = "id") Integer id, @RequestBody ChangePwRequest cqRequest, HttpServletRequest request) {
	    Optional<Employee> uEmployee = employeeService.getById(id);
		if(uEmployee.isPresent()){
			Principal principal = request.getUserPrincipal();
			if(uEmployee.get().getUsername().compareTo(principal.getName())==0){
				if(passwordEncoder.matches(cqRequest.getPassword(), uEmployee.get().getPassword())){
					uEmployee.get().setPassword(passwordEncoder.encode(cqRequest.getNewPassword()));
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
	@ResponseBody
	@PutMapping("/api/employee/admin-change")
	public ResponseEntity<ResponseObject> changePasswordByAdmin(@RequestBody AdChangePwRequest request) {
		Employee uEmployee = employeeService.getByUsername(request.getUsername())
			.orElseThrow(() -> new RuntimeException("Error: User not found."));
		uEmployee.setPassword(passwordEncoder.encode(request.getNewPassword()));
		employeeService.save(uEmployee);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Update employee password successfully", ""),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	@PutMapping("/api/employee/admin-change-role")
	public ResponseEntity<ResponseObject> changeRoleByAdmin(@RequestBody AdChangeRoleRequest request) {
		Employee uEmployee = employeeService.getByUsername(request.getUsername())
			.orElseThrow(() -> new RuntimeException("Error: User not found."));
		Set<String> strRoles = request.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_SALES_EMPLOYEE)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
						break;

					case "sales_admin":
						Role saRole = roleRepository.findByName(ERole.ROLE_SALES_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(saRole);
						break;

					case "sales_employee":
						Role seRole = roleRepository.findByName(ERole.ROLE_SALES_EMPLOYEE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(seRole);
						break;

					case "warehouse_admin":
						Role waRole = roleRepository.findByName(ERole.ROLE_WAREHOUSE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(waRole);
						break;

					case "warehouse_employee":
						Role weRole = roleRepository.findByName(ERole.ROLE_WAREHOUSE_EMPLOYEE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(weRole);
						break;
					
					case "delivery_admin":
						Role daRole = roleRepository.findByName(ERole.ROLE_DELIVERY_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(daRole);
						break;

					case "delivery_man":
						Role dmRole = roleRepository.findByName(ERole.ROLE_DELIVERY_MAN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(dmRole);
						break;

					default:
						Role r = roleRepository.findByName(ERole.ROLE_SALES_EMPLOYEE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(r);
				}
			});
		}
		uEmployee.setRoles(roles);
		employeeService.save(uEmployee);
		return new ResponseEntity<>(
			new ResponseObject("ok", "Update employee password successfully", ""),
			HttpStatus.OK);
}

	@PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
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
