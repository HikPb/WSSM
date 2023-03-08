package com.project.wsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.wsms.payload.request.LoginRequest;
import com.project.wsms.repository.EmployeeRepository;
import com.project.wsms.security.JwtUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
@CrossOrigin(origins = "*", maxAge = 3600) 
@Controller
public class MainController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String viewLogin(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}

	//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute LoginRequest loginRequest, BindingResult result, HttpServletResponse response){
        try {
			if(result.hasErrors()){
				return "login";
			}
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(),
					loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtUtils.generateToken(authentication);
			response.setHeader("Authorization", "Bearer " + token);
			return "redirect:/overview";
		} catch(Exception e){
			System.out.println(e.getMessage());
			return "index";
		}
    }

	// @ResponseBody
	// @PostMapping("/login")
    // public ResponseEntity<?> handleLogin(@Valid @RequestBody LoginRequest loginRequest){
	// 	try{
	// 		Authentication authentication = authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(
    //             loginRequest.getUsername(),
	// 			loginRequest.getPassword()
	// 			));
	// 		SecurityContextHolder.getContext().setAuthentication(authentication);
	// 		String token = jwtUtils.generateToken(authentication);
	// 		System.out.println(token);
	// 		//response.setHeader("Token", token);
	// 		return new ResponseEntity<>(token, HttpStatus.OK);
	// 	} catch(Exception e){
	// 		e.printStackTrace();
	// 		return new ResponseEntity<>(loginRequest, HttpStatus.BAD_REQUEST);
	// 	}
    // }

	@GetMapping("/overview")
	public String viewOverview(Model model) {
		model.addAttribute("pageTitle", "TỔNG QUAN");
		return "overview";
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/logout")
	public String viewLogout() {
		return "index";
	}
}
