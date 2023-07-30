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
import java.util.Date;
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
import com.project.wsms.model.Warehouse;
import com.project.wsms.repository.MessageRepository;
import com.project.wsms.service.CategoryService;
import com.project.wsms.service.CheckQtyService;
import com.project.wsms.service.CqItemService;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.ExportItemService;
import com.project.wsms.service.ExportService;
import com.project.wsms.service.ImportItemService;
import com.project.wsms.service.ImportService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.ProductService;
import com.project.wsms.service.SupplierService;
import com.project.wsms.service.WarehouseService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class WarehouseControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
	private WarehouseService warehouseService;

	@MockBean
	private EmployeeService employeeService;

	@MockBean
	private SupplierService supplierService;

	@MockBean
	private ProductService productService;
	
	@MockBean
	private ItemService itemService;

	@MockBean
	private ImportService importService;
	
	@MockBean
	private ExportService exportService;
	
	@MockBean
	private CheckQtyService checkqtyService;

	@MockBean
	private ImportItemService iItemService;
	
	@MockBean
	private ExportItemService eItemService;
	
	@MockBean
	private CqItemService cqItemService;

	@MockBean
	private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Warehouse warehouse;
    private Warehouse warehouse2;

    @BeforeEach
    public void init() {
        warehouse = new Warehouse();
        warehouse.setId(1);
        warehouse.setName("Test");
        warehouse.setPhone("0999888777");
        warehouse.setAddress("Test Test");
        warehouse.setCreatedAt(new Date());
        warehouse.setUpdatedAt(new Date());
        warehouse.setCheckqtys(new HashSet<>());
        warehouse.setExports(new HashSet<>());
        warehouse.setImports(new HashSet<>());
        warehouse.setOrders(new HashSet<>());

        warehouse2 = new Warehouse();
        warehouse2.setId(2);
        warehouse2.setName("Test2");
        warehouse2.setPhone("0999888999");
        warehouse2.setAddress("Test2 Test2");
        warehouse2.setCreatedAt(new Date());
        warehouse2.setUpdatedAt(new Date());
        warehouse2.setCheckqtys(new HashSet<>());
        warehouse2.setExports(new HashSet<>());
        warehouse2.setImports(new HashSet<>());
        warehouse2.setOrders(new HashSet<>());


    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void WarehouseController_CreateWarehouse_ReturnCreated() throws Exception {
        when(warehouseService.save(any(Warehouse.class))).thenReturn(warehouse);

        this.mockMvc.perform(post("/api/warehouse")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(warehouse)))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name", is(warehouse.getName())))
            .andExpect(jsonPath("$.data.phone", is(warehouse.getPhone())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void WarehouseController_FindAllWarehouse() throws Exception {
        List<Warehouse> listWarehouse = Arrays.asList(warehouse, warehouse2);
        when(warehouseService.getAll()).thenReturn(listWarehouse);
        this.mockMvc.perform(get("/api/warehouse")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.size()", is(listWarehouse.size())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void WarehouseController_WarehouseDetail() throws Exception {
        when(warehouseService.getById(anyInt())).thenReturn(Optional.of(warehouse));

        this.mockMvc.perform(get("/api/warehouse/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name", is(warehouse.getName())))
            .andExpect(jsonPath("$.data.phone", is(warehouse.getPhone())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void WarehouseController_UpdateWarehouse_ReturnWarehouseDto() throws Exception {
        when(warehouseService.save(any(Warehouse.class))).thenReturn(warehouse2);
        when(warehouseService.getById(anyInt())).thenReturn(Optional.of(warehouse));

        this.mockMvc.perform(put("/api/warehouse/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(warehouse2)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name", is(warehouse.getName())))
            .andExpect(jsonPath("$.data.phone", is(warehouse.getPhone())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"WAREHOUSE_ADMIN", "WAREHOUSE_EMPLOYEE"})
    public void WarehouseController_DeleteWarehouse_ReturnString() throws Exception {
        doNothing().when(warehouseService).delete(anyInt());
        when(warehouseService.existsById(anyInt())).thenReturn(Boolean.TRUE);

        this.mockMvc.perform(delete("/api/warehouse/{id}", Integer.valueOf(1)))
            .andExpect(status().isOk());
    }
}
