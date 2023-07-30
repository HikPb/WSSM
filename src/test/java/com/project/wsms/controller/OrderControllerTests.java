package com.project.wsms.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.project.wsms.dto.OrderDto;
import com.project.wsms.dto.OrderItemDto;
import com.project.wsms.model.Customer;
import com.project.wsms.model.ERole;
import com.project.wsms.model.Employee;
import com.project.wsms.model.Item;
import com.project.wsms.model.Message;
import com.project.wsms.model.Order;
import com.project.wsms.model.Warehouse;
import com.project.wsms.repository.MessageRepository;
import com.project.wsms.service.CustomerService;
import com.project.wsms.service.EmployeeService;
import com.project.wsms.service.ItemService;
import com.project.wsms.service.OrderItemService;
import com.project.wsms.service.OrderService;
import com.project.wsms.service.WarehouseService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class OrderControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private OrderItemService oItemService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ItemService itemService;

    @MockBean
	private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order1;
    private Order order2;
    private OrderDto orderDto;
    private OrderItemDto oItemDto;
    private Customer customer;
    private Employee employee;
    private Warehouse warehouse;

    @BeforeEach
    public void init() {
        customer = new Customer();
        customer.setId(1);
        customer.setName("Test");
        customer.setAddress("Test Test");

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

        employee = new Employee();
        employee.setId(1);
        employee.setFullname("Test");
        employee.setUsername("test");
        employee.setPhone("0999999999");
        employee.setPassword("testtest");
        employee.setRoles(new HashSet<>());
        employee.setOrders(new HashSet<>());
        employee.setImports(new HashSet<>());
        employee.setExports(new HashSet<>());
        employee.setCheckqtys(new HashSet<>());
        employee.setSentMess(new HashSet<>());
        employee.setReceivedMess(new HashSet<>());

        order1 = new Order();
        order1.setId(1);
        order1.setCreatedAt(new Date());
        order1.setUpdatedAt(new Date());
        order1.setCustomer(customer);
        order1.setEmployee(employee);
        order1.setInternalNote("");
        order1.setPrintedNote("");
        order1.setWarehouse(warehouse);
        order1.setDeliveryUnitId("");
        order1.setDiscount(0);
        order1.setOrderItems(new HashSet<>());
        order1.setOwe(0);
        order1.setTotal(0);
        order1.setProfit(0);
        order1.setSales(0);
        order1.setShippingFee(0);
        order1.setStatus(1);
        order1.setTotalWeight(0);
        order1.setRevenue(0);
        order1.setReceivedMoney(0);
        order1.setTotalDiscount(0);
        order1.setAddress("Test Test");
        order1.setReceiverName("Test");
        order1.setReceiverPhone("0999999999");
    

        order2 = new Order();
        order2.setId(2);
        order2.setCreatedAt(new Date());
        order2.setUpdatedAt(new Date());
        order2.setCustomer(customer);
        order2.setEmployee(employee);
        order2.setInternalNote("");
        order2.setPrintedNote("");
        order2.setWarehouse(warehouse);
        order2.setDeliveryUnitId("");
        order2.setDiscount(0);
        order2.setOrderItems(new HashSet<>());
        order2.setOwe(0);
        order2.setTotal(0);
        order2.setProfit(0);
        order2.setSales(0);
        order2.setShippingFee(0);
        order2.setStatus(1);
        order2.setTotalWeight(0);
        order2.setRevenue(0);
        order2.setReceivedMoney(0);
        order2.setTotalDiscount(0);
        order2.setAddress("Test Test");
        order2.setReceiverName("Test");
        order2.setReceiverPhone("0999999999");

        orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setStatus(1);
        orderDto.setPrintNote("");
        orderDto.setInternalNote("");
        orderDto.setDeliveryUnitId("");
        orderDto.setAddress("Test Test");
        orderDto.setReceiverName("Test");
        orderDto.setReceiverPhone("0988776655");
        orderDto.setDiscount(0);
        orderDto.setTotalWeight(0);
        orderDto.setShippingFee(0);
        orderDto.setTotalDiscount(0);
        orderDto.setReceivedMoney(0);
        orderDto.setOwe(0);
        orderDto.setTotal(0);
        orderDto.setRevenue(0);
        orderDto.setSales(0);
        orderDto.setProfit(0);
        orderDto.setItems(new HashSet<>());
        orderDto.setCusId(1);
        orderDto.setEmpId(1);
        orderDto.setWareId(1);
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"SALES_ADMIN", "SALES_EMPLOYEE", "WAREHOUSE_EMPLOYEE"})
    public void OrderController_CreateOrder_ReturnCreated() throws Exception {
        doNothing().when(orderService).save(any(Order.class));
        doNothing().when(employeeService).save(any(Employee.class));
        when(customerService.save(any(Customer.class))).thenReturn(customer);
        when(warehouseService.save(any(Warehouse.class))).thenReturn(warehouse);
        when(customerService.getById(anyInt())).thenReturn(Optional.of(customer));
        when(employeeService.getById(anyInt())).thenReturn(Optional.of(employee));
        when(warehouseService.getById(anyInt())).thenReturn(Optional.of(warehouse));
        when(employeeService.getByUsername(anyString())).thenReturn(Optional.of(employee));
        when(employeeService.getByRole(any(ERole.class))).thenReturn(Arrays.asList());
        when(messageRepository.save(any(Message.class))).thenReturn(null);

        this.mockMvc.perform(post("/api/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderDto)))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.receiverName", is(orderDto.getReceiverName())))
            .andExpect(jsonPath("$.data.owe", is(orderDto.getOwe())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"SALES_ADMIN", "SALES_EMPLOYEE", "WAREHOUSE_EMPLOYEE"})
    public void OrderController_FindAllOrder() throws Exception {
        List<Order> listOrder = Arrays.asList(order1, order2);
        when(orderService.getAll()).thenReturn(listOrder);
        this.mockMvc.perform(get("/api/order")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.size()", is(listOrder.size())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"SALES_ADMIN", "SALES_EMPLOYEE", "WAREHOUSE_EMPLOYEE"})
    public void OrderController_OrderDetail() throws Exception {
        when(orderService.getById(anyInt())).thenReturn(Optional.of(order1));

        this.mockMvc.perform(get("/api/order/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.receiverName", is(order1.getReceiverName())))
            .andExpect(jsonPath("$.data.owe", is(order1.getOwe())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"SALES_ADMIN", "SALES_EMPLOYEE", "WAREHOUSE_EMPLOYEE"})
    public void OrderController_UpdateOrder_ReturnOrderDto() throws Exception {
        doNothing().when(orderService).save(any(Order.class));
        when(orderService.existsById(anyInt())).thenReturn(Boolean.TRUE);
        when(orderService.getById(anyInt())).thenReturn(Optional.of(order1));
        when(customerService.save(any(Customer.class))).thenReturn(customer);
        when(warehouseService.save(any(Warehouse.class))).thenReturn(warehouse);
        when(customerService.getById(anyInt())).thenReturn(Optional.of(customer));
        when(warehouseService.getById(anyInt())).thenReturn(Optional.of(warehouse));

        this.mockMvc.perform(put("/api/order/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.receiverName", is(orderDto.getReceiverName())))
            .andExpect(jsonPath("$.data.owe", is(orderDto.getOwe())));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = {"SALES_ADMIN", "SALES_EMPLOYEE", "WAREHOUSE_EMPLOYEE"})
    public void OrderController_ChangeStatusOrder() throws Exception {
        doNothing().when(orderService).save(any(Order.class));
        doNothing().when(employeeService).save(any(Employee.class));
        when(itemService.save(any(Item.class))).thenReturn(null);
        when(customerService.save(any(Customer.class))).thenReturn(customer);
        when(orderService.existsById(anyInt())).thenReturn(Boolean.TRUE);
        when(orderService.getById(anyInt())).thenReturn(Optional.of(order1));
        when(employeeService.getByUsername(anyString())).thenReturn(Optional.of(employee));
        when(employeeService.getByRole(any(ERole.class))).thenReturn(Arrays.asList());
        when(messageRepository.save(any(Message.class))).thenReturn(null);

        this.mockMvc.perform(post("/api/order/{id}/status/{st}", 1,2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status", is(2)));
    }
}
