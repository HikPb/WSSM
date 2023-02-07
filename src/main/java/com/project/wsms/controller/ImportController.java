package com.project.wsms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.model.Import;
import com.project.wsms.model.ResponseObject;
import com.project.wsms.service.ImportService;

@Controller
@RequestMapping("/import")
public class ImportController {

	@Autowired
	private ImportService ipService;
	
	// VIEW NHẬP KHO
	@GetMapping
	public String getAllImportProduct(Model model) {
		// model.addAttribute("products", ipService.getAll());
		model.addAttribute("pageTitle", "NHẬP KHO");
		return "warehouse/import";
	}
	
	@GetMapping("/api")
	@ResponseBody
	public List<Import> findImportForms() {
		return ipService.getAll();
	}

	@GetMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable Integer id) {
		Boolean exists = ipService.existsById(id);
		if (exists) {
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Query import form successfully", ipService.getById(id).get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseObject>(
				new ResponseObject("failed", "Cannot find import form with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

//	@GetMapping("/api/search/")
//	@ResponseBody
//	public ResponseEntity<ResponseObject> findProductByKeyword(@RequestParam("keyword") String keyword) {
//		try {
//			List<ImportProduct> resultList = ipService.getByIpFormKeyword(keyword);
//			return new ResponseEntity<ResponseObject>(
//					new ResponseObject("ok", "Successfully searched by keyword: "+keyword, resultList), 
//					HttpStatus.OK
//					);
//		} catch (Exception e) {
//			return new ResponseEntity<ResponseObject>(
//					new ResponseObject("failed", "Exception when searching for: "+keyword, ""), 
//					HttpStatus.BAD_REQUEST
//					);
//		}
//	}
	
	@PostMapping("/api/new")
	public ResponseEntity<ResponseObject> createIpForm(@RequestBody Import ipForm) {
		try {
			ipService.save(ipForm);
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Create new successfully", ipForm), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("failed", "Exception when saving new", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
//	@PostMapping("/api/{id}/{isSell}")
//	@ResponseBody
//	public ResponseEntity<ResponseObject> updateProductIsSell(@PathVariable("id") String id, @PathVariable("isSell") boolean isSell) {
//		try {
//			ipService.updateIsSell(id, isSell);
//
//			String status = isSell ? "activated" : "inactivated";
//			String message = "Product id = " + id + " " + "has been "+ status;
//			return new ResponseEntity<ResponseObject>(
//					new ResponseObject("ok", message, ""), 
//					HttpStatus.OK
//					);
//		} catch (Exception e) {
//			return new ResponseEntity<ResponseObject>(
//					new ResponseObject("failed", "Exception when changing product/issell", ""), 
//					HttpStatus.BAD_REQUEST
//					);
//		}
//	}

	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable("id") Integer id) {
		Boolean exists = ipService.existsById(id);
		if (exists) {
			ipService.delete(id);
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Delete form successfully", ""),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseObject>(
				new ResponseObject("failed", "Cannot find form to delete", ""),
				HttpStatus.NOT_FOUND);
	}

	@PutMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> updateOne(@RequestBody Import ipForm, @PathVariable Integer id) {
		Import update = ipService.getById(id)
		.map( p-> {
//			p.setWarehouseId(ipForm.getWarehouseId());
//			p.setSupplierId(ipForm.getSupplierId());
//			p.setUpdated_at(LocalDateTime.now());
			ipService.update(p);
			return p;

		}).orElseGet(() -> {
			ipService.save(ipForm);
			return ipForm;
		});
		return new ResponseEntity<ResponseObject>(
			new ResponseObject("ok", "Update form successfully", update),
			HttpStatus.OK
			);
	}
}
