package com.project.wsms.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.dto.OrderDto;
import com.project.wsms.dto.OrderItemDto;
import com.project.wsms.model.Customer;
import com.project.wsms.model.ERole;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Item;
import com.project.wsms.model.Message;
import com.project.wsms.model.Order;
import com.project.wsms.model.OrderItem;
import com.project.wsms.model.Warehouse;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.repository.MessageRepository;
import com.project.wsms.service.CustomerService;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.OrderItemService;
import com.project.wsms.service.OrderService;
import com.project.wsms.service.ProductService;
import com.project.wsms.service.WarehouseService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private MessageRepository messageRepository;
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")
	@GetMapping("/order")
	public String getAllOrderProduct(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "QUẢN LÝ ĐƠN HÀNG");
		model.addAttribute("wareData", warehouseService.getAll());
		model.addAttribute("itemData", itemService.getAll());
		return "orders/order";
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE')")
	@GetMapping("/order/new")
	public String createOrder(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "TẠO ĐƠN HÀNG");
		model.addAttribute("wareData", warehouseService.getAll());
		model.addAttribute("itemData", itemService.getAll());

		return "orders/new-order";
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('DELIVERY_MAN') or hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/api/order")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllOrder(){
		try{
			List<Order> lists= orderService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", lists), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query import form data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('DELIVERY_MAN') or hasRole('SALES_EMPLOYEE') ")
	@GetMapping("/api/order/status")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOrderByStatus(@RequestParam(name="status-list") List<Integer> statusList ){
		try{
			List<Order> lists= orderService.getByStatusIn(statusList);
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", lists), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query import form data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('DELIVERY_MAN') or hasRole('SALES_EMPLOYEE')")
	@GetMapping("/api/order/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOneOrder(@PathVariable Integer id) {
		Optional<Order> object = orderService.getById(id);
		if (object.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query import form successfully", object.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find import form with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE')")
	@ResponseBody
	@GetMapping("/api/order/customer/{id}")
	public ResponseEntity<ResponseObject> getOrderByCustomer(@PathVariable("id") Integer id) {
		try{
			List<Order> listOrder = orderService.getByCustomer(id);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query item successfully", listOrder),
					HttpStatus.OK);		
		} catch (Exception e){
			return new ResponseEntity<>(
				new ResponseObject("Failed", "Error when searching with id = ", ""),
				HttpStatus.BAD_REQUEST);
		}		
	}

	@PreAuthorize("hasRole('SALES_ADMIN')")
	@PostMapping("/api/order/{id}/status/{st}")
	@ResponseBody
	public ResponseEntity<ResponseObject> changeOrderStatus(@PathVariable("id") Integer id, @PathVariable("st") Integer st, HttpServletRequest request) {
		try {
			if(orderService.existsById(id)){
				Order object = orderService.getById(id).get();
				if(object.getStatus()==1){
					switch(st){
						case 0:		//Moi->Huy don
							object.setStatus(st);
							Customer customer = object.getCustomer();
							customer.setTmoney(customer.getTmoney() - object.getReceivedMoney()); //hoan tien cho khach
							customer.setTowe(customer.getTowe() - object.getOwe());
							customer.setNpCus(customer.getNpCus() - 1);
							customerService.save(customer);
							break;
						case 2:		//Moi->Chuan bi hang
							object.setStatus(st);
							object.getOrderItems().forEach(it->{
								Item item = it.getItem();
								item.setQty(item.getQty()-it.getQty());
								itemService.save(item);
							});
							break;
						case 3: 	//Moi->Gui hang di
							object.setStatus(st);
							object.getOrderItems().forEach(it->{
								Item item = it.getItem();
								item.setQty(item.getQty()-it.getQty());
								itemService.save(item);
							});
							Employee sender = employeeService.getByUsername(request.getUserPrincipal().getName())
								.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
							List<Employee> listRecipient = employeeService.getByRole(ERole.ROLE_DELIVERY_MAN);
							listRecipient.stream().forEach(recip ->{
								Message message = new Message();
								message.setRead(false);
								message.setUrl("/export");
								message.setMessage("Đơn hàng cần gửi đi!");
								sender.addSentMess(message);
								recip.addReceivedMess(message);
								messageRepository.save(message);
								employeeService.save(recip);
								employeeService.save(sender);
							});
							break;
						default:
							break;
						}
				}

				if(object.getStatus()==2){
					switch(st){
						case 0:    //Chuan bi hang -> huy don
							object.setStatus(st);
							object.getOrderItems().forEach(it->{
								Item item = it.getItem();
								item.setQty(item.getQty()+it.getQty());
								itemService.save(item);
							});
							Customer customer = object.getCustomer();
							customer.setTmoney(customer.getTmoney() - object.getReceivedMoney()); //hoan tien cho khach
							customer.setTowe(customer.getTowe() - object.getOwe());
							customer.setNpCus(customer.getNpCus() - 1);
							customerService.save(customer);
							break;
						case 1:   //chuan bi hang -> moi
							object.setStatus(st);
							object.getOrderItems().forEach(it->{
								Item item = it.getItem();
								item.setQty(item.getQty()+it.getQty());
								itemService.save(item);
							});
							break;
						case 3: //chuan bi hang -> dang gui hang
							object.setStatus(st);
							Employee sender = employeeService.getByUsername(request.getUserPrincipal().getName())
								.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
							List<Employee> listRecipient = employeeService.getByRole(ERole.ROLE_DELIVERY_MAN);
							listRecipient.stream().forEach(recip ->{
								Message message = new Message();
								message.setRead(false);
								message.setUrl("/delivery");
								message.setMessage("Đơn hàng cần gửi đi!");
								sender.addSentMess(message);
								recip.addReceivedMess(message);
								messageRepository.save(message);
								employeeService.save(recip);
								employeeService.save(sender);
							});
							break;
						default:
							break;
					}
				}
				// if(object.getStatus()==3 && st==0){
				// 	object.setStatus(st);
				// 	object.getOrderItems().forEach(it->{
				// 		Item item = it.getItem();
				// 		item.setQty(item.getQty()+it.getQty());
				// 		itemService.save(item);
				// 	});
				// }
				orderService.save(object);
				return new ResponseEntity<>(
					new ResponseObject("ok", "Change status successfully", object),
					HttpStatus.OK);
			}else{
				return new ResponseEntity<>(
					new ResponseObject("ok", "Object does not exist", ""),
					HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
				new ResponseObject("false", "Problem when changing status", ""),
				HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE')")
	@PostMapping("/api/order")
	@ResponseBody
	public ResponseEntity<ResponseObject> createOrderForm(@RequestBody OrderDto objectDto, HttpServletRequest request) {
		try {
			Order newObject = objectDto.convertToEntity();
			
			Employee employee = employeeService.getById(objectDto.getEmpId()).get();			
			employee.addOrder(newObject);

			Warehouse wh = warehouseService.getById(objectDto.getWareId()).get();			
			wh.addOrder(newObject);

			Customer cus = customerService.getById(objectDto.getCusId()).get();			
			cus.addOrder(newObject);
			cus.setNpCus(cus.getNpCus() + 1);
			cus.setTmoney(cus.getTmoney()+newObject.getReceivedMoney());
			cus.setTowe(cus.getTowe()+ newObject.getOwe());

			// newObject.setEmployee(employee);
			// newObject.setWarehouse(wh);
			// newObject.setCustomer(cus);
			orderService.save(newObject);

			if(!objectDto.getItems().isEmpty()){
				objectDto.getItems().forEach(it ->{
					OrderItem newItem = it.convertToEntity();
					Item item = itemService.getById(it.getItemId()).get();
					item.addOrderItem(newItem);
					newObject.addOrderItem(newItem);
					
					orderItemService.save(newItem);
					itemService.save(item);
				});
				orderService.save(newObject);
			}
			employeeService.save(employee);
			warehouseService.save(wh);
			customerService.save(cus);

			Employee sender = employeeService.getByUsername(request.getUserPrincipal().getName())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
			List<Employee> listRecipient = employeeService.getByRole(ERole.ROLE_SALES_ADMIN);
			listRecipient.stream().forEach(recip ->{
				Message message = new Message();
				message.setRead(false);
				message.setUrl("/order");
				message.setMessage("Đơn hàng mới đã được tạo!");
				sender.addSentMess(message);
				recip.addReceivedMess(message);
				messageRepository.save(message);
				employeeService.save(recip);
				employeeService.save(sender);
			});
	
			return new ResponseEntity<>(
					new ResponseObject("ok", "Create new order successfully", newObject), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new order", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}

	@PreAuthorize("hasRole('SALES_EMPLOYEE')")
	@PutMapping("/api/order/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> editOrderForm(@PathVariable("id") Integer id, @RequestBody OrderDto objectDto) {
		try {
			if(orderService.existsById(id)){
				Order uObject = orderService.getById(id).get();

				Customer oldcus = uObject.getCustomer();
				oldcus.setNpCus(oldcus.getNpCus() - 1);
				oldcus.setTmoney(oldcus.getTmoney() - uObject.getReceivedMoney());
				oldcus.setTowe(oldcus.getTowe() - uObject.getOwe());

				uObject.setPrintedNote(objectDto.getPrintNote());
				uObject.setInternalNote(objectDto.getInternalNote());
				uObject.setDeliveryUnitId(objectDto.getDeliveryUnitId());
				uObject.setAddress(objectDto.getAddress());
				uObject.setReceiverName(objectDto.getReceiverName());
				uObject.setReceiverPhone(objectDto.getReceiverPhone());
				uObject.setDiscount(objectDto.getDiscount());
				uObject.setTotalWeight(objectDto.getTotalWeight());
				uObject.setShippingFee(objectDto.getShippingFee());
				uObject.setTotalDiscount(objectDto.getTotalDiscount());
				uObject.setReceivedMoney(objectDto.getReceivedMoney());
				uObject.setOwe(objectDto.getOwe());
				uObject.setTotal(objectDto.getTotal());
				uObject.setRevenue(objectDto.getRevenue());
				uObject.setSales(objectDto.getSales());
				uObject.setProfit(objectDto.getProfit());


				if(uObject.getWarehouse().getId() != objectDto.getWareId()){ //Thay doi warehouse
					Warehouse oldwh = uObject.getWarehouse();
					oldwh.removeOrder(id);
					Warehouse wh = warehouseService.getById(objectDto.getWareId()).get();			
					wh.addOrder(uObject);
					warehouseService.save(oldwh);
					warehouseService.save(wh);
				}

				if(uObject.getCustomer().getId() != objectDto.getCusId()){ //Thay doi customer
					oldcus.removeOrder(id);
					Customer cus = customerService.getById(objectDto.getCusId()).get();			
					cus.addOrder(uObject);
					cus.setNpCus(cus.getNpCus() + 1);
					cus.setTmoney(cus.getTmoney() + uObject.getReceivedMoney());
					cus.setTowe(cus.getTowe() + uObject.getOwe());
					customerService.save(oldcus);
					customerService.save(cus);
				}else{
					oldcus.setNpCus(oldcus.getNpCus() + 1);
					oldcus.setTmoney(oldcus.getTmoney() + uObject.getReceivedMoney());
					oldcus.setTowe(oldcus.getTowe() + uObject.getOwe());
					customerService.save(oldcus);
				}
				
				List<Integer> list = objectDto.getItems().stream().map(OrderItemDto::getId).collect(Collectors.toList());
				List<Integer> outList = uObject.getOrderItems().stream().filter(i -> !list.contains(i.getId())).map(OrderItem::getId).collect(Collectors.toList());
				if(!outList.isEmpty()){
					outList.forEach(o->{
						uObject.removeOrderItem(o);
						orderItemService.delete(o);
					});
				}
				if(!objectDto.getItems().isEmpty()){
					objectDto.getItems().forEach(it ->{
						if(it.getId() == null){
							OrderItem newItem = it.convertToEntity();
							Item item = itemService.getById(it.getItemId()).get();
							item.addOrderItem(newItem);
							uObject.addOrderItem(newItem);
							orderItemService.save(newItem);
							itemService.save(item);
						}else{
							OrderItem orderItem = orderItemService.getById(it.getId()).get();
							orderItem.setSku(it.getSku());
							orderItem.setPrice(it.getSprice());
							orderItem.setDiscount(it.getDiscount());
							orderItem.setQty(it.getQty());
							orderItemService.save(orderItem);
						}
						
					});
				}
				orderService.save(uObject);
				return new ResponseEntity<>(
					new ResponseObject("ok", "Edit import form successfully", uObject), 
					HttpStatus.OK
					);
			}else{
				return new ResponseEntity<>(
					new ResponseObject("ok", "Order form does not exist", ""), 
					HttpStatus.OK
					);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when save edited import form", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
}
