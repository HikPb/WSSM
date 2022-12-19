package com.project.wsms.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.wsms.collection.Product;
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

//	@GetMapping
//	public String getAll(Model model, @RequestParam(required = false) String keyword,
//      @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
//		try {
//		  List<Product> products = new ArrayList<Product>();
//		  Pageable paging = PageRequest.of(page - 1, size);
//		
//		  Page<Product> pageProducts;
//		  if (keyword == null) {
//		    pageProducts = productService.getAll(paging);
//		  } else {
//		    pageProducts = productService.getByProductNameStartingWith(keyword, paging);
//		    model.addAttribute("keyword", keyword);
//		  }
//		
//		  products = pageProducts.getContent();
//		  model.addAttribute("products", products);
//		  model.addAttribute("currentPage", pageProducts.getNumber() + 1);
//		  model.addAttribute("totalItems", pageProducts.getTotalElements());
//		  model.addAttribute("totalPages", pageProducts.getTotalPages());
//		  model.addAttribute("pageSize", size);
//		  } catch (Exception e) {
//			  model.addAttribute("message", e.getMessage());
//		  }
//
//		return "products/products";
//  }

	  @GetMapping("/create")
	  public String addProduct(Model model) {
	    model.addAttribute("product", new Product());
	    return "products/product_form";
	  }

	  @ModelAttribute("product")
	  public Product newProduct() {
	      return new Product();
	  }
	  @PostMapping("/create")
	  public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
	    try {
	    	product.setIsSell(true);
	    	product.setCreated_at(LocalDateTime.now());
	    	productService.save(product);
	    	redirectAttributes.addFlashAttribute("message", "The new product has been saved successfully!");
	    } catch (Exception e) {
	    	redirectAttributes.addAttribute("message", e.getMessage());
	    }

	    	return "redirect:/products";
	  }
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
	  @GetMapping("/delete/{id}")
	  public String deleteProduct(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
	    try {
	      productService.delete(id);
	      System.out.println("Delete Sucess");
	      redirectAttributes.addFlashAttribute("message", "The Product with id=" + id + " has been deleted successfully!");
	    } catch (Exception e) {
	      redirectAttributes.addFlashAttribute("message", e.getMessage());
	    }

	    return "redirect:/products";
	  }

	  @RequestMapping(value="/update", method = {RequestMethod.PUT, RequestMethod.GET})
	  public String update(Product product) {
		  Product uproduct = productService.getByProductId(product.getProductId()).get();
		  uproduct.setProductName(product.getProductName());
		  uproduct.setCateId(product.getCateId());
		  uproduct.setBarcode(product.getBarcode());
		  uproduct.setImportPrice(product.getImportPrice());
		  uproduct.setSellPrice(product.getSellPrice());
		  productService.update(uproduct);
		  return "redirect:/products";
	  }
	  
	  @PostMapping("/{id}/issell/{isSell}")
	  @ResponseBody
	  public String updateProductIsSell(@PathVariable("id") String id, @PathVariable("isSell") boolean isSell,
	      Model model, RedirectAttributes redirectAttributes) {
	    try {
	      productService.updateIsSell(id, isSell);

	      String status = isSell ? "mở" : "tắt";
	      String message = "Sản phẩm id=" + id + " đã được " + status;

	      redirectAttributes.addFlashAttribute("message", message);
	    } catch (Exception e) {
	      redirectAttributes.addFlashAttribute("message", e.getMessage());
	    }

	    return "redirect:/products";
	  }

	  @PostMapping("/api")
	  @ResponseBody
	  public List<Product> findProducts(){
		  return productService.getAll();
	  }
	  
	  @GetMapping("/api/search/")
	  @ResponseBody
	  public ResponseEntity<List<Product>> findProductByKeyword(@RequestParam("keyword") String keyword){
		  List<Product> resultList = productService.getByProductKeyword(keyword);
		  return ResponseEntity.ok(resultList);
	  }
	  
	  @GetMapping("/api/{id}")
	  @ResponseBody
	  public Product getOne(@PathVariable String id) {
		  Product product = productService.getByProductId(id).get();
		  return product;
	  }

//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable String id) {
//        productService.delete(id);
//    }
}
