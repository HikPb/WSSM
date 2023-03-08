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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.wsms.model.Category;
import com.project.wsms.payload.response.ResponseObject;
import com.project.wsms.service.CategoryService;

import jakarta.validation.Valid;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/category/search")
    public String search(@RequestParam("key") String key, Model model) {
        List<Category> results = categoryService.getByKeyword(key);
        model.addAttribute("results", results);
        return "products/product-form";
    }
	
	@GetMapping("/api/category")
	@ResponseBody
	public ResponseEntity<ResponseObject> listAllCategory(){
		List<Category> listCategory= categoryService.getAll();
		if(listCategory.isEmpty()) {
			return new ResponseEntity<>(
					new ResponseObject("empty", "No content", ""),
					HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(
				new ResponseObject("ok", "Query successfully", listCategory), 
				HttpStatus.OK);
	}
	
	@GetMapping("/api/category/search")
	@ResponseBody
	public ResponseEntity<ResponseObject> searchCategory(@RequestParam("key") String key){
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
	
	@PostMapping("/api/category/")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveCategory(@Valid @RequestBody Category category) {
		try {
			Category newCategory = new Category();
			newCategory.setCateName(category.getCateName());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Save new category successfully", categoryService.save(newCategory)), 
					HttpStatus.BAD_REQUEST
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseObject("failed", "Exception when saving new category", ""), 
					HttpStatus.BAD_REQUEST
					);
		}
	}
	
	@PutMapping("/api/category/{id}")
	public ResponseEntity<ResponseObject> updateCategory(@PathVariable Integer id, 
	                                        @RequestBody Category category) {
		
		Optional<Category> uCategory = categoryService.getById(id);
		if(uCategory.isPresent()){
					
			uCategory.get().setCateName(category.getCateName());
			return new ResponseEntity<>(
					new ResponseObject("ok", "Update category successfully", categoryService.save(uCategory.get())),
					HttpStatus.OK
					);
		
		}
		Category newCategory = new Category();
		newCategory.setCateName(category.getCateName());
		return new ResponseEntity<>(
				new ResponseObject("ok", "Update category successfully", categoryService.save(newCategory)),
				HttpStatus.OK
				);
	}
	
	@DeleteMapping("/api/category/{id}")
	public ResponseEntity<ResponseObject> deleteCategory(@PathVariable(value = "id") Integer id) {
	    if(!categoryService.existsById(id)) {
	    	return new ResponseEntity<>(
					new ResponseObject("failed", "Cannot find it to delete", ""),
					HttpStatus.NOT_FOUND);
	    }

	    categoryService.delete(id);
	    return new ResponseEntity<>(
				new ResponseObject("ok", "Delete category successfully", ""),
				HttpStatus.OK);
	}

}