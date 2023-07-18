package com.project.wsms.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.wsms.dto.ProductDto;
import com.project.wsms.model.Product;
import com.project.wsms.service.CategoryService;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.ProductService;
import com.project.wsms.service.SupplierService;
import com.project.wsms.service.WarehouseService;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private SupplierService SupplierService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;
    private Product product2;
    private ProductDto productDto;

    @BeforeEach
    public void init() {
        product = Product.builder()
            .productName("Test")
            .barcode("test")
            .weight(Float.valueOf(100))
            .categories(null)
            .link("link")
            .note("note")
            .items(new HashSet<>())
            .build();

        product2 = Product.builder()
            .productName("Test2")
            .barcode("test2")
            .weight(Float.valueOf(100))
            .categories(null)
            .link("link2")
            .note("note2")
            .items(new HashSet<>())
            .build();

        productDto = new ProductDto();
        productDto.setProductName("Test");
        productDto.setBarcode("test");
        productDto.setWeight(Float.valueOf(100));
        productDto.setCategories(new HashSet<>());
        productDto.setLink("link");
        productDto.setNote("note");
        productDto.setItems(new HashSet<>());
    }

    @Test
    public void ProductController_CreateProduct_ReturnCreated() throws Exception {
        doNothing().when(productService).save(any(Product.class));

        this.mockMvc.perform(post("/api/products/new")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto)))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName", is(productDto.getProductName())))
            .andExpect(jsonPath("$.barcode", is(productDto.getBarcode())));
    }

    // @Test
    // public void ProductController_FindAllProduct() throws Exception {
    //     List<Product> listProduct = new ArrayList<>();
    //     listProduct.add(product);
    //     listProduct.add(product2);
    //     when(productService.getAll()).thenReturn(listProduct);
    //     this.mockMvc.perform(get("/api/products"))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$.size()", is(listProduct.size())));
    // }

    // @Test
    // public void ProductController_ProductDetail() throws Exception {
    //     when(productService.getByProductId(anyInt()).get()).thenReturn(product);

    //     this.mockMvc.perform(get("/api/products/{id}", 1))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$.productName", is(productDto.getProductName())))
    //         .andExpect(jsonPath("$.barcode", is(productDto.getBarcode())));
    // }

    // @Test
    // public void ProductController_UpdateProduct_ReturnProductDto() throws Exception {
    //     doNothing().when(productService).save(any(Product.class));

    //     this.mockMvc.perform(put("/api/products/{id}", 1)
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(objectMapper.writeValueAsString(product)))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$.productName", is(productDto.getProductName())))
    //         .andExpect(jsonPath("$.barcode", is(productDto.getBarcode())));
    // }

    // @Test
    // public void ProductController_DeleteProduct_ReturnString() throws Exception {
    //     doNothing().when(productService).deleteProductId(1);

    //     ResultActions response = mockMvc.perform(delete("/api/product/1/delete")
    //             .contentType(MediaType.APPLICATION_JSON));

    //     response.andExpect(MockMvcResultMatchers.status().isOk());
    // }
}
