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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.dto.CheckQtyDto;
import com.project.wsms.dto.CqItemDto;
import com.project.wsms.dto.EmployeeDto;
import com.project.wsms.dto.ExportDto;
import com.project.wsms.dto.ExportItemDto;
import com.project.wsms.dto.ImportDto;
import com.project.wsms.dto.ImportItemDto;
import com.project.wsms.exception.NotFoundException;
import com.project.wsms.model.CheckQty;
import com.project.wsms.model.CqItem;
import com.project.wsms.model.ERole;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Export;
import com.project.wsms.model.ExportItem;
import com.project.wsms.model.Import;
import com.project.wsms.model.ImportItem;
import com.project.wsms.model.Item;
import com.project.wsms.model.Message;
import com.project.wsms.model.Supplier;
import com.project.wsms.model.Warehouse;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.repository.MessageRepository;
import com.project.wsms.service.CheckQtyService;
import com.project.wsms.service.CqItemService;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.ExportItemService;
import com.project.wsms.service.ExportService;
import com.project.wsms.service.ImportItemService;
import com.project.wsms.service.ImportService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.ProductService;
import com.project.wsms.service.SupplierService;
import com.project.wsms.service.WarehouseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
public class WarehouseController {

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ItemService itemService;

	@Autowired
	private ImportService importService;
	
	@Autowired
	private ExportService exportService;
	
	@Autowired
	private CheckQtyService checkqtyService;

	@Autowired
	private ImportItemService iItemService;
	
	@Autowired
	private ExportItemService eItemService;
	
	@Autowired
	private CqItemService cqItemService;

	@Autowired
	private MessageRepository messageRepository;
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/warehouse")
    public String view(Model model, HttpServletRequest request){
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
        model.addAttribute("pageTitle", "QUẢN LÝ KHO HÀNG");
		return "warehouse/warehouse";
    }
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/warehouse")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllWarehouse(){
		try{
			List<Warehouse> listWarehouse= warehouseService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listWarehouse), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query warehouse data", ""),
					HttpStatus.BAD_REQUEST);
		}		
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/warehouse/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> searchWarehouse(@RequestParam("key") String key){
		try {
			List<Warehouse> listWarehouse = warehouseService.getByKeyword(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query categories successfully", listWarehouse), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE') or hasRole('SALES_EMPLOYEE')")

	@GetMapping("/api/warehouse/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Warehouse> warehouse = warehouseService.getById(id);
		if (warehouse.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", warehouse.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
	@PostMapping(value = "/api/warehouse", consumes = {"application/xml","application/json"})
	@ResponseBody
	public ResponseEntity<ResponseObject> saveWarehouse(@RequestBody Warehouse warehouse) {
		try {
			Warehouse newWarehouse = new Warehouse();
			newWarehouse.setName(warehouse.getName());
			newWarehouse.setPhone(warehouse.getPhone());
			newWarehouse.setAddress(warehouse.getAddress());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new warehouse successfully", warehouseService.save(newWarehouse)), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new warehouse", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PutMapping("/api/warehouse/{id}")
	public ResponseEntity<ResponseObject> updateWarehouse(@PathVariable Integer id, 
	                                        @RequestBody Warehouse warehouse) {
		
		Optional<Warehouse> uWarehouse = warehouseService.getById(id);
		if(uWarehouse.isPresent()){
					
			uWarehouse.get().setName(warehouse.getName());
			uWarehouse.get().setPhone(warehouse.getPhone());
			uWarehouse.get().setAddress(warehouse.getAddress());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update warehouse successfully", warehouseService.save(uWarehouse.get())),
					HttpStatus.OK
					);
		
		}
		Warehouse newWarehouse = new Warehouse();
		newWarehouse.setName(warehouse.getName());
		newWarehouse.setPhone(warehouse.getPhone());
		newWarehouse.setAddress(warehouse.getAddress());
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update warehouse successfully", warehouseService.save(newWarehouse)),
				HttpStatus.OK
				);
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
	@DeleteMapping("/api/warehouse/{id}")
	public ResponseEntity<ResponseObject> deleteWarehouse(@PathVariable(value = "id") Integer id) {
	    if(!warehouseService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    warehouseService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete warehouse successfully", ""),
				HttpStatus.OK);
	}

	// VIEW KIỂM KHO
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/checkqty")
	public String getAllCheckQuantity(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "KIỂM HÀNG");
		model.addAttribute("wareData", warehouseService.getAll());
		model.addAttribute("itemData", itemService.getAll());
		return "warehouse/check-quantity";
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/api/checkqty")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllCheckQtyForm(){
		try{
			List<CheckQty> listCq= checkqtyService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listCq), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query checkqty form data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/api/checkqty/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOneCheckQty(@PathVariable Integer id) {
		Optional<CheckQty> object = checkqtyService.getById(id);
		if (object.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query cq form successfully", object.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find cq form with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")
	@PostMapping("/api/checkqty/{id}/status/{st}")
	@ResponseBody
	public ResponseEntity<ResponseObject> changeCheckQtyStatus(@PathVariable("id") Integer id, @PathVariable("st") Integer st) {
		try {
			if(checkqtyService.existsById(id)){
				CheckQty object = checkqtyService.getById(id).get();
				if(object.getStatus()==1){
					switch(st){
						case 0:		//Moi -> Huy
							object.setStatus(st);
							break;
						case 2:		//Moi -> Da kiem
							object.setStatus(st);
							object.getItems().forEach(it->{
								Item item = it.getItem();
								item.setQty(it.getCr_qty());
								itemService.save(item);
							});
							break;
						default:
							break;
						}
				}
				checkqtyService.save(object);
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

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")
	@PostMapping("/api/checkqty")
	@ResponseBody
	@Transactional
	public ResponseEntity<ResponseObject> createCheckQtyForm(@RequestBody CheckQtyDto objectDto, HttpServletRequest request) {
		try {
			CheckQty newObject = objectDto.convertToEntity();
			
			//Employee employee = employeeService.getByUsername(objectDto.getEmpId()).get();
			Employee employee = employeeService.getById(objectDto.getEmpId()).get();			
			employee.addCheckQty(newObject);

			Warehouse wh = warehouseService.getById(objectDto.getWareId()).get();			
			wh.addCheckQty(newObject);

			//newObject.setEmployee(employee);
			//newObject.setWarehouse(wh);
			checkqtyService.save(newObject);
			if(!objectDto.getItems().isEmpty()){
				objectDto.getItems().forEach(it ->{
					CqItem newItem = it.convertToEntity();

					Item item = itemService.getById(it.getItemId()).get();
					item.addCqItem(newItem);
					newObject.addItem(newItem);
					//item.setQty(newItem.getCr_qty());

					//newItem.setItem(item);
					//newItem.setCqtyProduct(newObject);
					
					cqItemService.save(newItem);
					itemService.save(item);
				});
				checkqtyService.save(newObject);
			}
			
			employeeService.save(employee);
			warehouseService.save(wh);

			Employee sender = employeeService.getByUsername(request.getUserPrincipal().getName())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
			List<Employee> listRecipient = employeeService.getByRole(ERole.ROLE_WAREHOUSE_ADMIN);
			listRecipient.stream().forEach(recip ->{
				Message message = new Message();
				message.setRead(false);
				message.setUrl("/checkqty");
				message.setMessage("Phiếu kiểm hàng được tạo mới!");
				sender.addSentMess(message);
				recip.addReceivedMess(message);
				messageRepository.save(message);
				employeeService.save(recip);
				employeeService.save(sender);
			});
			

			return new ResponseEntity<>(
					new ResponseObject("ok", "Create new cq form successfully", newObject), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new cq form", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")
	@PutMapping("/api/checkqty/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> editCheckQtyForm(@PathVariable("id") Integer id, @RequestBody CheckQtyDto objectDto) {
		try {
			if(checkqtyService.existsById(id)){
				CheckQty uObject = checkqtyService.getById(id).get();
				if(uObject.getStatus()==1){
					uObject.setNote(objectDto.getNote());
					//uObject.setStatus(objectDto.getStatus());
					uObject.setCheckqtyAt(objectDto.getCheckqtyAt());

					List<Integer> list = objectDto.getItems().stream().map(CqItemDto::getId).collect(Collectors.toList());
					List<Integer> outList = uObject.getItems().stream().filter(i -> !list.contains(i.getId())).map(CqItem::getId).collect(Collectors.toList());
					if(!outList.isEmpty()){
						outList.forEach(o->{
							uObject.removeItem(o);
							cqItemService.delete(o);
						});
					}
					if(!objectDto.getItems().isEmpty()){
						objectDto.getItems().forEach(it ->{
							if(it.getId() == null){
								CqItem newItem = it.convertToEntity();
		
								Item item = itemService.getById(it.getItemId()).get();
								item.addCqItem(newItem);
								uObject.addItem(newItem);
			
								//newItem.setItem(item);
								//newItem.setCqtyProduct(uObject);
								
								cqItemService.save(newItem);
								itemService.save(item);
							}else{
								CqItem cqItem = cqItemService.getById(it.getId()).get();
								cqItem.setSku(it.getSku());
								cqItem.setBf_qty(it.getBfQty());
								cqItem.setCr_qty(it.getCrQty());
								cqItemService.save(cqItem);
							}
							
						});
					}
					checkqtyService.save(uObject);
					return new ResponseEntity<>(
						new ResponseObject("ok", "Edit cq form successfully", uObject), 
						HttpStatus.OK
						);
				}else{
					return new ResponseEntity<>(
						new ResponseObject("ok", "Cqform is completed or cancelled.", ""), 
						HttpStatus.OK
						);
				}
			}else{
				return new ResponseEntity<>(
					new ResponseObject("ok", "Cqform does not exist", ""), 
					HttpStatus.OK
					);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when save edited cqform", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}

	// VIEW NHAP KHO
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/import")
	public String getAllImportProduct(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		// model.addAttribute("products", ipService.getAll());
		model.addAttribute("pageTitle", "NHẬP HÀNG");
		model.addAttribute("wareData", warehouseService.getAll());
		model.addAttribute("itemData", itemService.getAll());
		model.addAttribute("supData", supplierService.getAll());

		return "warehouse/import";
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/api/import")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllImportForm(){
		try{
			List<Import> listIp= importService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listIp), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query import form data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/api/import/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOneImport(@PathVariable Integer id) {
		Optional<Import> object = importService.getById(id);
		if (object.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query import form successfully", object.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find import form with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PostMapping("/api/import/{id}/status/{st}")
	@ResponseBody
	public ResponseEntity<ResponseObject> changeImportStatus(@PathVariable("id") Integer id, @PathVariable("st") Integer st) {
		try {
			if(importService.existsById(id)){
				Import object = importService.getById(id).get();
				if(object.getStatus()==1){
					switch(st){
						case 0:		//1->0
							object.setStatus(st);
							break;
						case 2:		//1->2
							object.setStatus(st);
							object.getItems().forEach(it->{
								Item item = it.getItem();
								item.setQty(item.getQty() + it.getQty());
								item.setPurcharsePrice(it.getPurcharsePrice());
								itemService.save(item);
							});
							break;
						default:
							break;
						}
				}

				if(object.getStatus()==2 && st==0){
					object.setStatus(st);
					object.getItems().forEach(it->{
						Item item = it.getItem();
						item.setQty(item.getQty() - it.getQty());
						itemService.save(item);
					});
				}
				importService.save(object);
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
	
	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PostMapping("/api/import")
	@ResponseBody
	public ResponseEntity<ResponseObject> createImportForm(@RequestBody ImportDto objectDto, HttpServletRequest request) {
		try {
			Import newObject = objectDto.convertToEntity();
			
			Employee employee = employeeService.getById(objectDto.getEmpId()).get();			
			employee.addImport(newObject);

			Warehouse wh = warehouseService.getById(objectDto.getWareId()).get();			
			wh.addImport(newObject);

			if(objectDto.getSupId()!=null){
				Optional<Supplier> sup = supplierService.getById(objectDto.getSupId());	
				if(!sup.isEmpty()){
					sup.get().addImportItem(newObject);
					supplierService.save(sup.get());
				}		
			}
			

			importService.save(newObject);

			if(!objectDto.getItems().isEmpty()){
				objectDto.getItems().forEach(it ->{
					ImportItem newItem = it.convertToEntity();
					Item item = itemService.getById(it.getItemId()).get();
					
					item.addImportItem(newItem);
					newObject.addItem(newItem);
					//item.setQty(item.getQty() + newItem.getQty());
					//item.setPurcharsePrice(newItem.getPurcharsePrice());
					if(newObject.getSupplier()!=null && !item.getProduct().getSuppliers().contains(newObject.getSupplier())){
						item.getProduct().addSupplier(newObject.getSupplier());
						productService.save(item.getProduct());
					}
			
					iItemService.save(newItem);
					itemService.save(item);
				});
				importService.save(newObject);
			}
			employeeService.save(employee);
			warehouseService.save(wh);

			Employee sender = employeeService.getByUsername(request.getUserPrincipal().getName())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
			List<Employee> listRecipient = employeeService.getByRole(ERole.ROLE_WAREHOUSE_ADMIN);
			listRecipient.stream().forEach(recip ->{
				Message message = new Message();
				message.setRead(false);
				message.setUrl("/import");
				message.setMessage("Phiếu nhập kho mới đã được tạo!");
				sender.addSentMess(message);
				recip.addReceivedMess(message);
				messageRepository.save(message);
				employeeService.save(recip);
				employeeService.save(sender);
			});

			return new ResponseEntity<>(
					new ResponseObject("ok", "Create new import form successfully", newObject), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new import form", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PutMapping("/api/import/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> editImportForm(@PathVariable("id") Integer id, @RequestBody ImportDto objectDto) {
		try {
			if(importService.existsById(id)){
				Import uObject = importService.getById(id).get();
				uObject.setNote(objectDto.getNote());
        		//uObject.setStatus(objectDto.getStatus());
        		uObject.setExpected_at(objectDto.getExpectedAt());
				uObject.setTotalQty(objectDto.getTotalQty());
				uObject.setTotalMoney(objectDto.getTotalMoney());
				uObject.setShipFee(objectDto.getShipFee());

				if(objectDto.getSupId()!=null){
					if(uObject.getSupplier()==null){
					Supplier newSup = supplierService.getById(objectDto.getSupId()).get();
					newSup.addImportItem(uObject);
					supplierService.save(newSup);
					} else if(uObject.getSupplier().getId()!=objectDto.getSupId()){
						Supplier oldSup = uObject.getSupplier();
						oldSup.removeItem(uObject.getId());

						Supplier newSup = supplierService.getById(objectDto.getSupId()).get();
						newSup.addImportItem(uObject);
						supplierService.save(oldSup);
						supplierService.save(newSup);
					}
				}

				List<Integer> list = objectDto.getItems().stream().map(ImportItemDto::getId).collect(Collectors.toList());
				List<Integer> outList = uObject.getItems().stream().filter(i -> !list.contains(i.getId())).map(ImportItem::getId).collect(Collectors.toList());
				if(!outList.isEmpty()){
					outList.forEach(o->{
						uObject.removeItem(o);
						iItemService.delete(o);
					});
				}
				if(!objectDto.getItems().isEmpty()){
					objectDto.getItems().forEach(it ->{
						if(it.getId() == null){
							ImportItem newItem = it.convertToEntity();
							Item item = itemService.getById(it.getItemId()).get();
							
							item.addImportItem(newItem);
							uObject.addItem(newItem);
							
							iItemService.save(newItem);
							itemService.save(item);
						}else{
							ImportItem ipItem = iItemService.getById(it.getId()).get();
							ipItem.setSku(it.getSku());
							ipItem.setPurcharsePrice(it.getPprice());
							ipItem.setQty(it.getQty());
						}
						
					});
				}
				importService.save(uObject);
				return new ResponseEntity<>(
					new ResponseObject("ok", "Edit import form successfully", uObject), 
					HttpStatus.OK
					);
			}else{
				return new ResponseEntity<>(
					new ResponseObject("ok", "Import form does not exist", ""), 
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

	// VIEW XUẤT KHO

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/export")
	public String getAllExportProduct(Model model, HttpServletRequest request) {
		Principal user = request.getUserPrincipal();
		Employee emp = employeeService.getByUsername(user.getName()).get();
		EmployeeDto employee = new EmployeeDto();
		employee.convertToDto(emp);
		model.addAttribute("user", employee);
		model.addAttribute("pageTitle", "XUẤT HÀNG");
		model.addAttribute("wareData", warehouseService.getAll());
		model.addAttribute("itemData", itemService.getAll());
		model.addAttribute("supData", supplierService.getAll());
		return "warehouse/export";
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/api/export")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllExportForm(){
		try{
			List<Export> listEp= exportService.getAll();
			return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listEp), 
				HttpStatus.OK);
		} catch(Exception e){
			return new ResponseEntity<>(
					new ResponseObject("failed", "Error when query export form data", ""),
					HttpStatus.BAD_REQUEST);
		}	
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@GetMapping("/api/export/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOneExport(@PathVariable Integer id) {
		Optional<Export> object = exportService.getById(id);
		if (object.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query export form successfully", object.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find export form with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PostMapping("/api/export/{id}/status/{st}")
	@ResponseBody
	public ResponseEntity<ResponseObject> changeExportStatus(@PathVariable("id") Integer id, @PathVariable("st") Integer st) {
		Export object = exportService.getById(id)
			.orElseThrow(() -> new NotFoundException("Not found export object with id = " + id));
		
			if(object.getStatus()==1){
			switch(st){
				case 0:		//1->0
					object.setStatus(st);
					break;
				case 2:		//1->2
					if(object.checklistItem()){
						object.setStatus(st);
						object.getItems().forEach(it->{
							Item item = it.getItem();
							item.setQty(item.getQty() - it.getQty());
							itemService.save(item);
						});
					}else{
						return new ResponseEntity<>(
							new ResponseObject("false", "Cannot be changed, the inventory is not enough.", ""),
							HttpStatus.OK);
					}
					break;
				default:
					break;
				}
		}

		if(object.getStatus()==2 && st==0){		//2->0
			object.setStatus(st);
			object.getItems().forEach(it->{
				Item item = it.getItem();
				item.setQty(item.getQty() + it.getQty());
				itemService.save(item);
			});
		}
		exportService.save(object);
		return new ResponseEntity<>(
			new ResponseObject("ok", "Change status successfully", object),
			HttpStatus.OK);
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PostMapping("/api/export")
	@ResponseBody
	public ResponseEntity<ResponseObject> createExportForm(@RequestBody ExportDto objectDto, HttpServletRequest request) {
		try {
			Export newObject = objectDto.convertToEntity();
			
			Employee employee = employeeService.getById(objectDto.getEmpId()).get();			
			employee.addExport(newObject);

			Warehouse wh = warehouseService.getById(objectDto.getWareId()).get();			
			wh.addExport(newObject);

			// newObject.setEmployee(employee);
			// newObject.setWarehouse(wh);
			exportService.save(newObject); //Tranh loi not-null property a transient value

			if(!objectDto.getItems().isEmpty()){
				objectDto.getItems().forEach(it ->{
					ExportItem newItem = it.convertToEntity();

					Item item = itemService.getById(it.getItemId()).get();
					item.addExportItem(newItem);
					newObject.addItem(newItem);
					//item.setQty(item.getQty() - newItem.getQty());
					//item.setRetailPrice(newItem.getRetailPrice());

					// newItem.setItem(item);
					// newItem.setExportProduct(newObject);
					eItemService.save(newItem);
					itemService.save(item);
				});
				exportService.save(newObject);
			}
			
			employeeService.save(employee);
			warehouseService.save(wh);

			Employee sender = employeeService.getByUsername(request.getUserPrincipal().getName())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
			List<Employee> listRecipient = employeeService.getByRole(ERole.ROLE_WAREHOUSE_ADMIN);
			listRecipient.stream().forEach(recip ->{
				Message message = new Message();
				message.setRead(false);
				message.setUrl("/export");
				message.setMessage("Phiếu xuất kho mới đã được tạo!");
				sender.addSentMess(message);
				recip.addReceivedMess(message);
				messageRepository.save(message);
				employeeService.save(recip);
				employeeService.save(sender);
			});

			return new ResponseEntity<>(
					new ResponseObject("ok", "Create new export form successfully", newObject), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new export form", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}

	@PreAuthorize("hasRole('WAREHOUSE_EMPLOYEE')")

	@PutMapping("/api/export/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> editExportForm(@PathVariable("id") Integer id, @RequestBody ExportDto objectDto) {
		try {
			if(exportService.existsById(id)){
				Export uObject = exportService.getById(id).get();
				uObject.setNote(objectDto.getNote());
        		//uObject.setStatus(objectDto.getStatus());
				uObject.setTotalQty(objectDto.getTotalQty());
				uObject.setTotalMoney(objectDto.getTotalMoney());
        		uObject.setExpected_at(objectDto.getExpectedAt());

				List<Integer> list = objectDto.getItems().stream().map(ExportItemDto::getId).collect(Collectors.toList());
				List<Integer> outList = uObject.getItems().stream().filter(i -> !list.contains(i.getId())).map(ExportItem::getId).collect(Collectors.toList());
				if(!outList.isEmpty()){
					outList.forEach(o->{
						uObject.removeItem(o);
						eItemService.delete(o);
					});
				}
				if(!objectDto.getItems().isEmpty()){
					objectDto.getItems().forEach(it ->{
						if(it.getId() == null){
							ExportItem newItem = it.convertToEntity();
	
							Item item = itemService.getById(it.getItemId()).get();
							item.addExportItem(newItem);
							uObject.addItem(newItem);
							// newItem.setItem(item);
							// newItem.setExportProduct(uObject);
							
							eItemService.save(newItem);
							itemService.save(item);
						}else{
							ExportItem epItem = eItemService.getById(it.getId()).get();
							epItem.setSku(it.getSku());
							epItem.setRetailPrice(it.getSprice());
							epItem.setQty(it.getQty());
							eItemService.save(epItem);
						}
						
					});
				}
				exportService.save(uObject);
				return new ResponseEntity<>(
					new ResponseObject("ok", "Edit export form successfully", uObject), 
					HttpStatus.OK
					);
			}else{
				return new ResponseEntity<>(
					new ResponseObject("ok", "Export form does not exist", ""), 
					HttpStatus.OK
					);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when save edited export form", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	//

}
