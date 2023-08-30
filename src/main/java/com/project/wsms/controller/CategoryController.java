package com.project.wsms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.project.wsms.exception.NotFoundException;
import com.project.wsms.model.Category;
import com.project.wsms.model.Product;
import com.project.wsms.payload.request.CategoryRequest;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.repository.ProductRepository;
import com.project.wsms.service.CategoryService;
import com.project.wsms.service.ProductService;

import jakarta.validation.Valid;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/category/search")
    public String search(@RequestParam("key") String key, Model model) {
        List<Category> results = categoryService.getByKeyword(key);
        model.addAttribute("results", results);
        return "products/product-form";
    }
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/api/category")
	@ResponseBody
	public ResponseEntity<ResponseObject> getAll(){
		List<Category> listCategory= categoryService.getAll();
		return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listCategory), 
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/api/category/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> getByKey(@RequestParam("key") String key){
		try {
			List<Category> listCategory = categoryService.getByKeyword(key);
			return new ResponseEntity<>( 
					new ResponseObject("ok", "Query categories successfully", listCategory), 
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when searching for: "+ key, ""), 
					HttpStatus.BAD_REQUEST
					);
		}		
	}
	
	@PreAuthorize("hasRole('SALES_EMPLOYEE') or hasRole('WAREHOUSE_EMPLOYEE')")
	@GetMapping("/api/category/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getOne(@PathVariable("id") Integer id) {
		Optional<Category> category = categoryService.getById(id);
		if (category.isPresent()) {
			return new ResponseEntity<>(
					new ResponseObject("ok", "Query product successfully", category.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseObject("failed", "Cannot find product with id = " + id, ""),
				HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
	@PostMapping("/api/category")
	@ResponseBody
	public ResponseEntity<ResponseObject> create(@Valid @RequestBody String category) {
		try {
			Category newCategory = new Category();
			newCategory.setCateName(category);
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new category successfully", categoryService.save(newCategory)), 
					HttpStatus.CREATED
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new category", ""), 
					HttpStatus.NOT_FOUND
					);
		}
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
	@PutMapping("/api/category")
	public ResponseEntity<ResponseObject> update(@RequestBody CategoryRequest category) {
		if(!categoryService.existsById(category.getId())){
			throw new NotFoundException("Not found category with id = " + category.getId());
		}
		Category uCategory = categoryService.getById(category.getId()).get();
		uCategory.setCateName(category.getCateName());
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update category successfully", categoryService.save(uCategory)),
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
	@DeleteMapping("/api/category/{id}")
	public ResponseEntity<ResponseObject> delete(@PathVariable(value = "id") Integer id) {
	    if(!categoryService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

		List<Product> listProducts = productService.getByCategoryId(id);
		if(!listProducts.isEmpty()){
			listProducts.forEach(p->{
				p.removeCategory(id);
			});
		}
	    categoryService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete category successfully", ""),
				HttpStatus.OK);
	}

}