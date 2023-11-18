package com.nobita.springboot.bankwebappapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobita.springboot.bankwebappapi.models.Customer;
import com.nobita.springboot.bankwebappapi.models.dto.AddCustomerDTO;
import com.nobita.springboot.bankwebappapi.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCustomers() {
    }

    @Test
    void getCustomerById() {
    }

    @Test
    void addCustomerValidPanTest() throws Exception {
        Customer addCustomer = new Customer(1, "Naruto", null);
        when(customerService.addCustomer(any(Customer.class))).thenReturn(addCustomer);

        AddCustomerDTO customerDTO = new AddCustomerDTO("Naruto", null);

        MvcResult perform = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andReturn();
//                .andExpect(status().isOk());
        System.out.println(perform.getResponse().getStatus());

        verify(customerService, times(1)).addCustomer(any());
    }

    @Test
    void editCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void testEditCustomer() {
    }
}