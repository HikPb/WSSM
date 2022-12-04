package com.project.wsms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.wsms.collection.Product;
import com.project.wsms.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
//	@GetMapping
//	public List<Product> findProducts(){
//		return productService.getAll();
//	}

	@GetMapping
	public String getAll(Model model, @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
		try {
		  List<Product> products = new ArrayList<Product>();
		  Pageable paging = PageRequest.of(page - 1, size);
		
		  Page<Product> pageProducts;
		  if (keyword == null) {
		    pageProducts = productService.getAll(paging);
		  } else {
		    pageProducts = productService.getByProductNameStartingWith(keyword, paging);
		    model.addAttribute("keyword", keyword);
		  }
		
		  products = pageProducts.getContent();
		  model.addAttribute("products", products);
		  model.addAttribute("currentPage", pageProducts.getNumber() + 1);
		  model.addAttribute("totalItems", pageProducts.getTotalElements());
		  model.addAttribute("totalPages", pageProducts.getTotalPages());
		  model.addAttribute("pageSize", size);
		  } catch (Exception e) {
			  model.addAttribute("message", e.getMessage());
		  }

		return "products/products";
  }

	  @GetMapping("/products/create")
	  public String addProduct(Model model) {
	    Product product = new Product();
	    model.addAttribute("product", product);
	    model.addAttribute("pageTitle", "Create Product");

	    return "products/product_form";
	  }
//
//	  @PostMapping("/products/save")
//	  public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
//	    try {
//	      productService.save(product);
//
//	      redirectAttributes.addFlashAttribute("message", "The new product has been saved successfully!");
//	    } catch (Exception e) {
//	      redirectAttributes.addAttribute("message", e.getMessage());
//	    }
//
//	    return "redirect:/products";
//	  }
//
//	  @GetMapping("/products/{id}")
//	  public String editProduct(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
//	    try {
//	      Optional<Product> product = productService.getProductById(id);
//
//	      model.addAttribute("Product", product);
//	      model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
//
//	      return "product_form";
//	    } catch (Exception e) {
//	      redirectAttributes.addFlashAttribute("message", e.getMessage());
//
//	      return "redirect:/products";
//	    }
//	  }
//
//	  @GetMapping("/products/delete/{id}")
//	  public String deleteProduct(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
//	    try {
//	      productService.delete(id);
//
//	      redirectAttributes.addFlashAttribute("message", "The Product with id=" + id + " has been deleted successfully!");
//	    } catch (Exception e) {
//	      redirectAttributes.addFlashAttribute("message", e.getMessage());
//	    }
//
//	    return "redirect:/products";
//	  }

//	  @GetMapping("/products/{id}/published/{status}")
//	  public String updateProductPublishedStatus(@PathVariable("id") String id, @PathVariable("status") boolean published,
//	      Model model, RedirectAttributes redirectAttributes) {
//	    try {
//	      ProductRepository.updatePublishedStatus(id, published);
//
//	      String status = published ? "published" : "disabled";
//	      String message = "The Product id=" + id + " has been " + status;
//
//	      redirectAttributes.addFlashAttribute("message", message);
//	    } catch (Exception e) {
//	      redirectAttributes.addFlashAttribute("message", e.getMessage());
//	    }
//
//	    return "redirect:/products";
//	  }
	@PostMapping
    public void save(@RequestBody Product product) {
        productService.save(product);
    }

	@GetMapping("/{id}")
    public Optional<Product> getByProductId(@PathVariable String id) {
        return productService.getByProductId(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}
