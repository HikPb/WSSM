package com.project.wsms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.wsms.model.ProductCategory;
import com.project.wsms.service.ProductCategoryService;

@RestController
@RequestMapping("/api")
public class RestApiController {

	@Autowired
	private ProductCategoryService categoryService;
	
	@GetMapping("/category")
	public ResponseEntity<List<ProductCategory>> listAllCategory(){
		List<ProductCategory> listCategory= categoryService.getAll();
		if(listCategory.isEmpty()) {
			return new ResponseEntity<List<ProductCategory>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ProductCategory>>(listCategory, HttpStatus.OK);
	}
	
	@GetMapping("/category/search")
	public ResponseEntity<List<ProductCategory>> searchCategory(@RequestParam("key") String key){
		List<ProductCategory> listCategory= categoryService.getByKeyword(key);
		if(listCategory.isEmpty()) {
			return new ResponseEntity<List<ProductCategory>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ProductCategory>>(listCategory, HttpStatus.OK);
	}
	
	@GetMapping("/category/{id}")
	public ProductCategory findByCategoryId(@PathVariable("id") String id) {
		ProductCategory category= categoryService.getOne(id).get();
		if(category == null) {
			ResponseEntity.notFound().build();
		}
		return category;
	}
	
	@PostMapping("/category/")
	public void saveCategory(@Valid @RequestBody ProductCategory category) {
		categoryService.save(category);
	}
	
	@PutMapping("/category/")
	public ResponseEntity<ProductCategory> updateCategory(@PathVariable(value = "cateId") String cateId, 
	                                       @Valid @RequestBody ProductCategory categoryForm) {
		ProductCategory category = categoryService.getOne(cateId).get();
	    if(category == null) {
	        return ResponseEntity.notFound().build();
	    }
	    category.setCateName(categoryForm.getCateName());
	    ProductCategory updatedCategory = categoryService.save(category);
	    return ResponseEntity.ok(updatedCategory);
	}
	
	@DeleteMapping("/category/{id}")
	public ResponseEntity<ProductCategory> deleteCategory(@PathVariable(value = "cateId") String cateId) {
		ProductCategory category = categoryService.getOne(cateId).get();
	    if(category == null) {
	        return ResponseEntity.notFound().build();
	    }

	    categoryService.delete(category.getCateId());
	    return ResponseEntity.ok().build();
	}

}