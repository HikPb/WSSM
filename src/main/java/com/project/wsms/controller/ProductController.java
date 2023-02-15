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

import com.project.wsms.dto.ProductDto;
import com.project.wsms.model.Category;
import com.project.wsms.model.Item;
import com.project.wsms.model.Product;
import com.project.wsms.model.ResponseObject;
import com.project.wsms.model.Supplier;
import com.project.wsms.model.Warehouse;
import com.project.wsms.service.CategoryService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.ProductService;
import com.project.wsms.service.SupplierService;
import com.project.wsms.service.WarehouseService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private ItemService itemService;

	@GetMapping("/products")
	public String getAllProduct(Model model) {
		model.addAttribute("pageTitle", "QUẢN LÝ SẢN PHẨM");
		//model.addAttribute("warehouses", warehouseService.getAll());
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
	@ResponseBody
	public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductDto productDto) {
		try {
			Product newProduct = productDto.convertToEntity();
			newProduct.setTSale(0);
			newProduct.setTImport(0);
			newProduct.setTInventory(0);
			if(!productDto.getCategories().isEmpty()){
				productDto.getCategories().forEach(c->{
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
			}
			
			if(!productDto.getSuppliers().isEmpty()){
				productDto.getSuppliers().forEach(c->{
					if(supplierService.existsById(c.getId())) {
						Optional<Supplier> supplier = supplierService.getById(c.getId());
						newProduct.addSupplier(supplier.get());
					}
				});
			}

			if(!productDto.getItems().isEmpty()){
				productDto.getItems().forEach(it ->{
					Item newItem = it.convertToEntity();
					newProduct.setTImport(newProduct.getTImport() + it.getQty());
					newProduct.setTInventory(newProduct.getTInventory() + it.getQty());
					Warehouse wh = warehouseService.getById(it.getWareId()).get();
					newItem.setProduct(newProduct);
					newItem.setWarehouse(wh);
					newProduct.addItem(newItem);
					wh.addItem(newItem);
					warehouseService.save(wh);
					itemService.save(newItem);
				});

			}
			productService.save(newProduct);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Create new product successfully", newProduct), 
					HttpStatus.OK
					);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new product", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}


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
	public ResponseEntity<ResponseObject> updateOne(@RequestBody ProductDto productDto, @PathVariable Integer id) {
		Product uproduct = productService.getByProductId(id)
		.map( p-> {
			p.setBarcode(productDto.getBarcode());
			p.setProductName(productDto.getProductName());
			p.setWeight(productDto.getWeight());
			p.setLink(productDto.getLink());
			p.setNote(productDto.getNote());
			if(!p.getCategories().equals(productDto.getCategories())) {
				p.resetCategories();
				productDto.getCategories().forEach(c->{
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
			p.resetSuppliers();
			productDto.getSuppliers().forEach(c->{
				if(supplierService.existsById(c.getId())){
					Optional<Supplier> sup = supplierService.getById(c.getId());
					p.addSupplier(sup.get());
				}
			});

			productDto.getItems().forEach(i->{
				if(i.getId()==null){
					Item newItem = i.convertToEntity();
					p.setTImport(p.getTImport() + i.getQty());
					p.setTInventory(p.getTInventory() + i.getQty());
					Warehouse wh = warehouseService.getById(i.getWareId()).get();
					newItem.setProduct(p);
					newItem.setWarehouse(wh);
					p.addItem(newItem);
					wh.addItem(newItem);
					warehouseService.save(wh);
					itemService.save(newItem);
				} else {
					Optional<Item> it = itemService.getById(i.getId());
					p.setTInventory(p.getTInventory() - it.get().getQty() + i.getQty());
					it.get().setPurcharsePrice(i.getPprice());
					it.get().setRetailPrice(i.getSprice());
					it.get().setQty(i.getQty());
					it.get().setSku(i.getSku());
				}
			});
			productService.update(p);
			return p;

		}).orElseGet(() -> {
			Product newProduct = new Product();
			newProduct.setBarcode(productDto.getBarcode());
			newProduct.setProductName(productDto.getProductName());
			newProduct.setWeight(productDto.getWeight());
			newProduct.setLink(productDto.getLink());
			newProduct.setNote(productDto.getNote());
			productDto.getCategories().forEach(c->{
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
