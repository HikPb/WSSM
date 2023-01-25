package com.project.wsms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.wsms.collection.Product;
import com.project.wsms.collection.ResponseObject;
import com.project.wsms.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public String getAllProduct(Model model) {
		model.addAttribute("products", productService.getAll());
		model.addAttribute("pageTitle", "QUẢN LÝ SẢN PHẨM");
		return "products/products";
	}

	@GetMapping("/create")
	public String addProduct(Model model) {
		model.addAttribute("product", new Product());
		return "products/product_form";
	}

	@ModelAttribute("product")
	public Product newProduct() {
		return new Product();
	}

//	@PostMapping("/create")
//	public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
//		try {
//			product.setCreated_at(LocalDateTime.now());
//			productService.save(product);
//			redirectAttributes.addFlashAttribute("message", "The new product has been saved successfully!");
//		} catch (Exception e) {
//			redirectAttributes.addAttribute("message", e.getMessage());
//		}
//
//		return "redirect:/products";
//	}

	//
	// @GetMapping("/products/{id}")
	// public String editProduct(@PathVariable("id") String id, Model model,
	// RedirectAttributes redirectAttributes) {
	// try {
	// Optional<Product> product = productService.getProductById(id);
	//
	// model.addAttribute("Product", product);
	// model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
	//
	// return "product_form";
	// } catch (Exception e) {
	// redirectAttributes.addFlashAttribute("message", e.getMessage());
	//
	// return "redirect:/products";
	// }
	// }
	//
	@GetMapping("/api")
	@ResponseBody
	public List<Product> findProducts() {
		return productService.getAll();
	}

	@GetMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable String id) {
		Boolean exists = productService.existsById(id);
		if (exists) {
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Query product successfully", productService.getByProductId(id).get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseObject>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/search/")
	@ResponseBody
	public ResponseEntity<ResponseObject> findProductByKeyword(@RequestParam("keyword") String keyword) {
		try {
			List<Product> resultList = productService.getByProductKeyword(keyword);
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Successfully searched by keyword: "+keyword, resultList), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("failed", "Exception when searching for: "+keyword, ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PostMapping("/api/new")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody Product product) {
		try {
			product.setCreated_at(LocalDateTime.now());
			product.setUpdated_at(LocalDateTime.now());
			productService.save(product);
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Create new product successfully", product), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("failed", "Exception when saving new product", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PostMapping("/api/{id}/{isSell}")
	@ResponseBody
	public ResponseEntity<ResponseObject> updateProductIsSell(@PathVariable("id") String id, @PathVariable("isSell") boolean isSell) {
		try {
			productService.updateIsSell(id, isSell);

			String status = isSell ? "activated" : "inactivated";
			String message = "Product id = " + id + " " + "has been "+ status;
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", message, ""), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("failed", "Exception when changing product/issell", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}

	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable("id") String id) {
		Boolean exists = productService.existsById(id);
		if (exists) {
			productService.delete(id);
			return new ResponseEntity<ResponseObject>(
					new ResponseObject("ok", "Delete product successfully", ""),
					HttpStatus.OK);
		}
		return new ResponseEntity<ResponseObject>(
				new ResponseObject("failed", "Cannot find product to delete", ""),
				HttpStatus.NOT_FOUND);
	}

	@PutMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> updateOne(@RequestBody Product product, @PathVariable String id) {
		Product uproduct = productService.getByProductId(id)
		.map( p-> {
			p.setBarcode(product.getBarcode());
			p.setProductName(product.getProductName());
			p.setSellPrice(product.getSellPrice());
			p.setImportPrice(product.getImportPrice());
			p.setListCateId(product.getListCateId());
			p.setListWarehouseId(product.getListWarehouseId());
			p.setSupplierId(product.getSupplierId());
			p.setUpdated_at(LocalDateTime.now());
			productService.update(p);
			return p;

		}).orElseGet(() -> {
			productService.save(product);
			return product;
		});
		return new ResponseEntity<ResponseObject>(
			new ResponseObject("ok", "Update product successfully", uproduct),
			HttpStatus.OK
			);
	}

}
