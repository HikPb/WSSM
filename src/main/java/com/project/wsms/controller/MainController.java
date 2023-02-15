package com.project.wsms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/overview")
	public String overview(Model model) {
		model.addAttribute("pageTitle", "TỔNG QUAN");
		return "overview";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "index";
	}
}
