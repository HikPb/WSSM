package com.project.wsms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    public String view(Model model){
        model.addAttribute("pageTitle", "QUẢN LÝ KHO HÀNG");
		return "warehouse";
    }
}
