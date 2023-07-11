package com.project.wsms.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.exception.NotFoundException;
import com.project.wsms.model.Customer;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Item;
import com.project.wsms.model.Order;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.CustomerService;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.OrderService;
import com.project.wsms.service.SupplierService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DeliveryController {
	
	@Autowired
	private SupplierService supplierService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired 
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ItemService itemService;
	

	@PreAuthorize("hasRole('DELIVERY_MAN') or hasRole('DELIVERY_ADMIN')")

	@GetMapping("/delivery")
    public String view(Model model, HttpServletRequest request){
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
        model.addAttribute("pageTitle", "QUẢN LÝ GIAO HÀNG");
		return "/delivery/delivery";
    }

	@PreAuthorize("hasRole('DELIVERY_MAN')")
	@PostMapping("/api/delivery/order-status")
	@ResponseBody
	public ResponseEntity<ResponseObject> changeOrderStatus(@RequestParam(name = "orderid") Integer id, @RequestParam("status") Integer status) {
		Order object = orderService.getById(id)
			.orElseThrow(() -> new NotFoundException("Not Found order"));
		if(object.getStatus()==3){
			switch(status){
				case 4:		//Dang giao -> Delay
					object.setStatus(status);
					break;
				case 5:		//Dang giao -> Giao thanh cong
					object.setStatus(status);
					Customer customer = object.getCustomer();
					customer.setTmoney(customer.getTmoney() + object.getOwe()); 
					customer.setTowe(customer.getTowe() - object.getOwe());
					customer.setNsoCus(customer.getNsoCus()+1);
					customerService.save(customer);
					break;
				default:
					break;
			}
		}

		if(object.getStatus()==4){
			switch(status){
				case 5:    //Delay -> Giao thanh cong
					object.setStatus(status);
					Customer customer = object.getCustomer();
					customer.setTmoney(customer.getTmoney() + object.getOwe());
					customer.setTowe(customer.getTowe() - object.getOwe());
					customer.setNsoCus(customer.getNsoCus()+1);
					customerService.save(customer);
					break;
				case 6:   //Delay -> Hoan hang
					object.setStatus(status);
					Customer customer2 = object.getCustomer();
					customer2.setTmoney(customer2.getTmoney() - object.getReceivedMoney());
					customer2.setTowe(customer2.getTowe() - object.getOwe());
					customer2.setNroCus(customer2.getNroCus()+1);
					customerService.save(customer2);
					break;
				default:
					break;
			}
		}

		if(object.getStatus()==6 && status == 7){		//Hoan hang -> Da hoan hang
			object.setStatus(status);
			object.getOrderItems().forEach((it) -> {
				Item item = it.getItem();
				item.setQty(item.getQty()+it.getQty());
				itemService.save(item);
			});
		}

		orderService.save(object);
		return new ResponseEntity<>(
			new ResponseObject("ok", "Delivery change order status successfully", ""), 
			HttpStatus.OK
			);
	}
}
