package com.nobita.springboot.bankwebappapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobita.springboot.bankwebappapi.Exceptions.InvalidFieldException;
import com.nobita.springboot.bankwebappapi.models.Customer;
import com.nobita.springboot.bankwebappapi.models.dto.AddCustomerDTO;
import com.nobita.springboot.bankwebappapi.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCustomers() throws Exception
    {
        List<Customer> customers = List.of(new Customer(1, "Naruto", null),
                new Customer(2, "Nobita", "NDSPS7223K"));
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].name").value("Naruto"));
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById() throws Exception
    {
        Customer addCustomer = new Customer(1, "Naruto", null);
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(addCustomer));

        mockMvc.perform(get("/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Naruto"));

        verify(customerService, times(1)).findByCustomerId(1);
    }

    @Test
    void getCustomerByIdNotFound() throws Exception
    {
        when(customerService.findByCustomerId(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).findByCustomerId(2);
    }

    @Test
    void addCustomerTest() throws Exception
    {
        Customer addCustomer = new Customer(1, "Naruto", null);
        when(customerService.addCustomer(any(Customer.class))).thenReturn(addCustomer);

        AddCustomerDTO customerDTO = new AddCustomerDTO("Naruto", null);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andExpect(status().is2xxSuccessful());

        verify(customerService, times(1)).addCustomer(any());
    }

    @Test
    void editCustomerNotFound() throws Exception {
        when(customerService.findByCustomerId(2)).thenReturn(Optional.empty());

        AddCustomerDTO customerDTO = new AddCustomerDTO("Naruto", null);

        mockMvc.perform(put("/customers/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                        .andExpect(status().isBadRequest());

        verify(customerService, times(1)).findByCustomerId(2);
    }

    @Test
    void editCustomer() throws Exception
    {
        Customer customer = new Customer(1, "Naruto", null);
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customer));

        AddCustomerDTO customerDTO = new AddCustomerDTO("Naruto", "SJFKS3253L");

        mockMvc.perform(put("/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.panNum").value("SJFKS3253L"));

        verify(customerService, times(1)).findByCustomerId(1);
    }

    @Test
    void deleteCustomerNotFound() throws Exception {
        when(customerService.findByCustomerId(2)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/customers/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                         .andExpect(status().isBadRequest());

        verify(customerService, times(1)).findByCustomerId(2);
        verify(customerService, times(0)).deleteCustomer(2);
    }

    @Test
    void deleteCustomer() throws Exception
    {
        Customer customer = new Customer(1, "Naruto", null);
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customer));

        mockMvc.perform(delete("/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).findByCustomerId(1);
        verify(customerService, times(1)).deleteCustomer(1);
    }

    @Test
    void editCustomerFieldsException() throws Exception
    {
        Customer customer = new Customer(1, "Naruto", null);
        when(customerService.findByCustomerId(anyInt())).thenReturn(Optional.of(customer));

        Map<String, Object> fields = new HashMap<>();
        fields.put("panNum", "SJFKS3253L");

        Customer updatedCustomer = new Customer(1, "Naruto", "SJFKS3253L");

        when(customerService.updateCustomerByFields(customer,fields)).thenReturn(updatedCustomer);

        mockMvc.perform(patch("/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fields)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.panNum").value("SJFKS3253L"));

        verify(customerService, times(1)).updateCustomerByFields(customer, fields);
    }

    @Test
    void editCustomerFieldsNotFound() throws Exception {
        when(customerService.findByCustomerId(1)).thenReturn(Optional.empty());

        Map<String, Object> fields = new HashMap<>();
        fields.put("panNum", "SJFKS3253L");

        mockMvc.perform(patch("/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fields)))
                        .andExpect(status().isBadRequest());

        verify(customerService, times(1)).findByCustomerId(1);
    }

    @Test
    void editCustomerFieldsInvalidField() throws Exception {
        Customer customer = new Customer(1, "Naruto", null);
        when(customerService.findByCustomerId(1)).thenReturn(Optional.of(customer));

        Map<String, Object> fields = new HashMap<>();
        fields.put("pandsNum", "SJFKS3253L");

        when(customerService.updateCustomerByFields(customer,fields)).thenThrow(InvalidFieldException.class);

        mockMvc.perform(patch("/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fields)))
                        .andExpect(status().isBadRequest());

        verify(customerService, times(1)).findByCustomerId(1);
        verify(customerService, times(1)).updateCustomerByFields(customer,fields);
    }
}