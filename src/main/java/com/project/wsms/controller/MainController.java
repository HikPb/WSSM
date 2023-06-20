package com.project.wsms.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.model.Employee;
import com.project.wsms.payload.request.LoginRequest;
import com.project.wsms.payload.response.JwtResponse;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.repository.EmployeeRepository;
import com.project.wsms.security.CustomUserDetails;
import com.project.wsms.security.JwtUtils;
import com.project.wsms.service.EmployeeService;

import jakarta.servlet.http.Cookie;
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

	// @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String viewLogin(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}

	// @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	// @PostMapping("/login")
	// public String handleLogin(@Valid @ModelAttribute LoginRequest loginRequest,
	// BindingResult result, HttpServletResponse response){
	// try {
	// if(result.hasErrors()){
	// return "login";
	// }
	// Authentication authentication = authenticationManager.authenticate(
	// new UsernamePasswordAuthenticationToken(
	// loginRequest.getUsername(),
	// loginRequest.getPassword()));
	// SecurityContextHolder.getContext().setAuthentication(authentication);
	// String token = jwtUtils.generateToken(authentication);
	// response.setHeader("Authorization", "Bearer " + token);
	// return "redirect:/overview";
	// } catch(Exception e){
	// System.out.println(e.getMessage());
	// return "index";
	// }
	// }

	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity<?> handleLogin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtUtils.generateToken(authentication);
			// response.setHeader("Token", token);
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			Cookie cookie = new Cookie("access-token", token);
			cookie.setHttpOnly(false);
			cookie.setSecure(false);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.addCookie(cookie);
			return new ResponseEntity<>(new ResponseObject("ok", "Login successfull",
					new JwtResponse(token, "Beare", userDetails.getUsername(), userDetails.getRole())), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseObject("failed", "Error when auth", ""), HttpStatus.OK);
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

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/logout")
	public String viewLogout() {
		return "index";
	}
}
