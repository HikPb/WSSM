package com.project.wsms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/warehouse")
public class WarehouseController {

	@GetMapping("/warehouse")
    public String view(Model model){
        model.addAttribute("pageTitle", "QUẢN LÝ KHO HÀNG");
		return "warehouse";
    }
    
	// VIEW KIỂM KHO
	@GetMapping("checkquantity")
	public String getAllCheckQuantity(Model model) {
		// model.addAttribute("products", productService.getAll());
		model.addAttribute("pageTitle", "KIỂM KHO");
		return "warehouse/check-quantity";
	}

	

	// VIEW XUẤT KHO

	@GetMapping("/export")
	public String getAllExportProduct(Model model) {
		// model.addAttribute("products", productService.getAll());
		model.addAttribute("pageTitle", "XUẤT KHO");
		return "warehouse/export";
	}
}
