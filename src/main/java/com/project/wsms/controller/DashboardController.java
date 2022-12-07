package com.project.wsms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/overview")
	public String overview(Model model) {
		model.addAttribute("pageTitle", "TỔNG QUAN");
		return "overview";
	}
		
}
