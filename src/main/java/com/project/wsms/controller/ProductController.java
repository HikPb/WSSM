package com.project.wsms.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.model.Category;
import com.project.wsms.model.Product;
import com.project.wsms.model.ResponseObject;
import com.project.wsms.service.CategoryService;
import com.project.wsms.service.ProductService;
import com.project.wsms.service.WarehouseService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private WarehouseService warehouseService;

	@GetMapping("/products")
	public String getAllProduct(Model model) {
		model.addAttribute("pageTitle", "QUẢN LÝ SẢN PHẨM");
		model.addAttribute("warehouses", warehouseService.getAll());
		return "products/products";
	}

//	@GetMapping("/products/{id}")
//	 public String editProduct(@PathVariable("id") Integer id, Model model) {
//		try {
//			Optional<Product> product = productService.getByProductId(id);
//		
//			model.addAttribute("Product", product.get());
//		
//			return "product_form";
//			} catch (Exception e) {
//				e.printStackTrace();
//				//return "redirect:/products";
//		 }
//		return "redirect:/products";
//	 }
	@GetMapping("/products/create")
	public String addProduct(Model model) {
		model.addAttribute("product", new Product());
		return "products/product_form";
	}

	@ModelAttribute("product")
	public Product newProduct() {
		return new Product();
	}

	@PostMapping("products/create")
	public String saveProduct(@ModelAttribute("product") Product product) {
		try {
			productService.save(product);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/products";
	}

	
	@GetMapping("/api/products")
	@ResponseBody
	public ResponseEntity<ResponseObject> findProducts() {
		List<Product> listProduct= productService.getAll();
		if(listProduct.isEmpty()) {
			return new ResponseEntity<>(
					new ResponseObject("empty", "No content", ""),
					HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listProduct), 
				HttpStatus.OK);
	}

	@GetMapping("/api/products/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable Integer id) {
		Optional<Product> product = productService.getByProductId(id);
		if (product.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", product.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/products/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> findProductByKeyword(@RequestParam("keyword") String keyword) {
		try {
			List<Product> resultList = productService.getByProductKeyword(keyword);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Successfully searched by keyword: "+keyword, resultList), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+keyword, ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PostMapping("/api/products/new")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody Product product) {
		try {
			Product newProduct = new Product();
			newProduct.setBarcode(product.getBarcode());
			newProduct.setProductName(product.getProductName());
			newProduct.setWeight(product.getWeight());
			product.getCategories().forEach(c->{
				if(categoryService.existsById(c.getId())) {
					Optional<Category> category = categoryService.getById(c.getId());
					newProduct.addCategory(category.get());
				}else {
					Category newCategory = new Category();
					newCategory.setCateName(c.getCateName());
					newProduct.addCategory(newCategory);
					categoryService.save(newCategory);
				}
				
			});
			productService.save(newProduct);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Create new product successfully", newProduct), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new product", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
//	@PostMapping("/api/{id}/{isSell}")
//	@ResponseBody
//	public ResponseEntity<ResponseObject> updateProductIsSell(@PathVariable("id") Integer id, @PathVariable("isSell") boolean isSell) {
//		try {
//			productService.updateIsSell(id, isSell);
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

	@DeleteMapping("/api/products/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable("id") Integer id) {
		Boolean exists = productService.existsById(id);
		if (Boolean.TRUE.equals(exists)) {
			productService.delete(id);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Delete product successfully", ""),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product to delete", ""),
				HttpStatus.NOT_FOUND);
	}

	@PutMapping("/api/products/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> updateOne(@RequestBody Product product, @PathVariable Integer id) {
		Product uproduct = productService.getByProductId(id)
		.map( p-> {
			p.setBarcode(product.getBarcode());
			p.setProductName(product.getProductName());
			p.setWeight(product.getWeight());
			if(!p.getCategories().equals(product.getCategories())) {
				p.resetCategory();
				product.getCategories().forEach(c->{
					if(categoryService.existsById(c.getId())) {
						Optional<Category> category = categoryService.getById(c.getId());
						p.addCategory(category.get());
					}else {
						Category newCategory = new Category();
						newCategory.setCateName(c.getCateName());
						p.addCategory(newCategory);
						categoryService.save(newCategory);
					}	
				});
			}
			productService.update(p);
			return p;

		}).orElseGet(() -> {
			Product newProduct = new Product();
			newProduct.setBarcode(product.getBarcode());
			newProduct.setProductName(product.getProductName());
			newProduct.setWeight(product.getWeight());
			product.getCategories().forEach(c->{
				if(categoryService.existsById(c.getId())) {
					Optional<Category> category = categoryService.getById(c.getId());
					newProduct.addCategory(category.get());
				}else {
					Category newCategory = new Category();
					newCategory.setCateName(c.getCateName());
					newProduct.addCategory(newCategory);
					categoryService.save(newCategory);
				}
				
			});
			productService.save(newProduct);
			return newProduct;
		});
		return new ResponseEntity<>(
			new ResponseObject("ok", "Update product successfully", uproduct),
			HttpStatus.OK
			);
	}

}
