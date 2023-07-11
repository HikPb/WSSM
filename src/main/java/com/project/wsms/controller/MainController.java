package com.project.wsms.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.config.WebSocketEventListener;
import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.model.Employee;
import com.project.wsms.payload.request.LoginRequest;
import com.project.wsms.payload.response.JwtResponse;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.payload.response.SbdResponse;
import com.project.wsms.payload.response.SbeResponse;
import com.project.wsms.payload.response.SbpResponse;
import com.project.wsms.payload.response.SbwResponse;
import com.project.wsms.repository.EmployeeRepository;
import com.project.wsms.repository.OrderRepository;
import com.project.wsms.repository.WarehouseRepository;
import com.project.wsms.security.CustomUserDetails;
import com.project.wsms.security.JwtUtils;
import com.project.wsms.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class MainController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private WarehouseRepository warehouseRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	// @PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_ADMIN') or hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")

	@GetMapping("/")
	public String index(HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		if(user!= null){
			return "redirect:/overview";
		}
		return "index";
	}

	@GetMapping("/login")
	public String viewLogin(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		if(user != null){
			return "redirect:/overview";
		}
		return "login";
	}
	
	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity<?> handleLogin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try{
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//String token = jwtUtils.generateToken(authentication);
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			ResponseCookie jwtCookie = JwtUtils.generateJwtCookie(authentication);
			List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
								.body(new ResponseObject("OK", "Login successfull",
										new JwtResponse(jwtCookie.toString(), "Beare", userDetails.getUsername(), roles)));
		} catch (UsernameNotFoundException e){
			return ResponseEntity.ok().body(new ResponseObject("USERNAME_NOT_FOUND", e.getMessage(), ""));
		} catch (BadCredentialsException e) {
			return ResponseEntity.ok().body(new ResponseObject("BAD_CREDENTIALS", e.getMessage(), ""));
		}
	}

	@GetMapping("/overview")
	public String viewOverview(Model model, HttpServletRequest request) {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "TỔNG QUAN");
		
		// String cookied = request.getHeader(HttpHeaders.COOKIE);
		return "overview";
	}

	@GetMapping("/statistic/revenue")
	public String viewStatisticRevenue(Model model, HttpServletRequest request) {

		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "BÁO CÁO DOANH THU");
		return "statistic/revenue";
	}

	@GetMapping("/statistic/sales")
	public String viewStatisticSales(Model model, HttpServletRequest request) {

		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "BÁO CÁO DOANH SỐ");
		return "statistic/sales";
	}

	@GetMapping("/statistic/order")
	public String viewStatisticOrder(Model model, HttpServletRequest request) {

		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "THỐNG KÊ ĐƠN HÀNG");
		return "statistic/order";
	}

	@PostMapping("/logout")
	public String viewLogout() {
		return "index";
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_ADMIN') or hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN') or hasRole('DELIVERY_MAN')")
	@GetMapping("/api/sbd")
	@ResponseBody
	public ResponseEntity<ResponseObject> getSbd(@RequestParam(value="start") Date startDate, @RequestParam(value="end") Date endDate){
		try{
			List<SbdResponse> lists= orderRepository.getStatisticsByDay(startDate, endDate);
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", lists), 
				HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_ADMIN') or hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN') or hasRole('DELIVERY_MAN')")

	@GetMapping("/api/sbw")
	@ResponseBody
	public ResponseEntity<ResponseObject> getSbw(@RequestParam(value="start") Date startDate, @RequestParam(value="end") Date endDate){
		try{
			List<SbwResponse> lists= warehouseRepository.getStatisticsByWarehouse(startDate, endDate);
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", lists), 
				HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_ADMIN') or hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN') or hasRole('DELIVERY_MAN')")

	@GetMapping("/api/sbe")
	@ResponseBody
	public ResponseEntity<ResponseObject> getSbe(@RequestParam(value="start") Date startDate, @RequestParam(value="end") Date endDate){
		try{
			List<SbeResponse> lists= employeeRepository.getStatisticsByEmployee(startDate, endDate);
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", lists), 
				HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_ADMIN') or hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN') or hasRole('DELIVERY_MAN')")
	@GetMapping("/api/sbp")
	@ResponseBody
	public ResponseEntity<ResponseObject> getSbp(@RequestParam(value="start") Date startDate, @RequestParam(value="end") Date endDate){
		try{
			List<SbpResponse> lists= orderRepository.getStatisticsByProduct(startDate, endDate);
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", lists), 
				HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}

}
