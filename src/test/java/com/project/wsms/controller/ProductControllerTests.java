package com.project.wsms.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

@SpringBootTest
@AutoConfigureMockMvc
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
            .id(1)
            .productName("Test")
            .barcode("test")
            .weight(Float.valueOf(100))
            .link("link")
            .note("note")
            .items(new HashSet<>())
            .categories(new HashSet<>())
            .suppliers(new HashSet<>())
            .build();

        product2 = Product.builder()
            .id(2)
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
        productDto.setBarcode("test1234");
        productDto.setWeight(Float.valueOf(100));
        productDto.setCategories(new HashSet<>());
        productDto.setLink("link");
        productDto.setNote("note");
        productDto.setItems(new HashSet<>());
        productDto.setSuppliers(new HashSet<>());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void ProductController_CreateProduct_ReturnCreated() throws Exception {
        doNothing().when(productService).save(any(Product.class));

        this.mockMvc.perform(post("/api/products/new")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto)))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.productName", is(productDto.getProductName())))
            .andExpect(jsonPath("$.data.barcode", is(productDto.getBarcode())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "WAREHOUSE_EMPLOYEE")
    public void ProductController_FindAllProduct() throws Exception {
        List<Product> listProduct = Arrays.asList(product, product2);
        when(productService.getAll()).thenReturn(listProduct);
        this.mockMvc.perform(get("/api/products")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.size()", is(listProduct.size())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "WAREHOUSE_EMPLOYEE")
    public void ProductController_ProductDetail() throws Exception {
        Optional<Product> op = Optional.of(product);
        when(productService.getByProductId(anyInt())).thenReturn(op);

        this.mockMvc.perform(get("/api/products/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.productName", is(product.getProductName())))
            .andExpect(jsonPath("$.data.barcode", is(product.getBarcode())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void ProductController_UpdateProduct_ReturnProductDto() throws Exception {
        doNothing().when(productService).save(any(Product.class));
        when(productService.getByProductId(anyInt())).thenReturn(Optional.of(product));

        this.mockMvc.perform(put("/api/products/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.productName", is(productDto.getProductName())))
            .andExpect(jsonPath("$.data.barcode", is(productDto.getBarcode())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void ProductController_DeleteProduct_ReturnString() throws Exception {
        doNothing().when(productService).delete(anyInt());
        when(productService.existsById(anyInt())).thenReturn(Boolean.TRUE);

        this.mockMvc.perform(delete("/api/products/{id}", Integer.valueOf(1)))
            .andExpect(status().isOk());
    }
}
