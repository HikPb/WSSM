package com.project.wsms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.wsms.collection.Order;
import com.project.wsms.service.OrderService;

@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping
	public String getAllOrder(Model model) {
		model.addAttribute("orders", orderService.getAll());
		model.addAttribute("pageTitle", "QUẢN LÝ ĐƠN HÀNG");
		return "orders/orders";
	}


	  @GetMapping("/create")
	  public String addOrder(Model model) {
	    model.addAttribute("order", new Order());
	    return "orders/order_form";
	  }

	  @ModelAttribute("order")
	  public Order newOrder() {
	      return new Order();
	  }
	  @PostMapping("/create")
	  public String saveOrder(@ModelAttribute("order") Order order, RedirectAttributes redirectAttributes) {
	    try {
//	    	order.setIsSell(true);
	    	order.setOrderTime(LocalDateTime.now());
	    	orderService.save(order);
	    	redirectAttributes.addFlashAttribute("message", "The new order has been saved successfully!");
	    } catch (Exception e) {
	    	redirectAttributes.addAttribute("message", e.getMessage());
	    }

	    	return "redirect:/orders";
	  }
//
//	  @GetMapping("/orders/{id}")
//	  public String editOrder(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
//	    try {
//	      Optional<Order> order = orderService.getOrderById(id);
//
//	      model.addAttribute("Order", order);
//	      model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
//
//	      return "order_form";
//	    } catch (Exception e) {
//	      redirectAttributes.addFlashAttribute("message", e.getMessage());
//
//	      return "redirect:/orders";
//	    }
//	  }
//
	  @GetMapping("/delete/{id}")
	  public String deleteOrder(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
	    try {
	      orderService.delete(id);
	      System.out.println("Delete Sucess");
	      redirectAttributes.addFlashAttribute("message", "The Order with id=" + id + " has been deleted successfully!");
	    } catch (Exception e) {
	      redirectAttributes.addFlashAttribute("message", e.getMessage());
	    }

	    return "redirect:/orders";
	  }

	  @RequestMapping(value="/update", method = {RequestMethod.PUT, RequestMethod.GET})
	  public String update(Order order) {
		  Order uorder = orderService.getByOrderId(order.getOrderId()).get();
//		  uorder.setOrderName(order.getOrderName());
//		  uorder.setCateId(order.getCateId());
//		  uorder.setBarcode(order.getBarcode());
//		  uorder.setImportPrice(order.getImportPrice());
//		  uorder.setSellPrice(order.getSellPrice());
		  orderService.update(uorder);
		  return "redirect:/orders";
	  }
	 

	  @PostMapping("/api")
	  @ResponseBody
	  public List<Order> findOrders(){
		  return orderService.getAll();
	  }
	  
	  @GetMapping("/api/{id}")
	  @ResponseBody
	  public Order getOne(@PathVariable String id) {
		  Order order = orderService.getByOrderId(id).get();
		  return order;
	  }
}
