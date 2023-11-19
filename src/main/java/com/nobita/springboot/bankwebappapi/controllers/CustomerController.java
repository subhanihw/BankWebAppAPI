package com.nobita.springboot.bankwebappapi.controllers;

import com.nobita.springboot.bankwebappapi.Exceptions.CustomerNotFoundException;
import com.nobita.springboot.bankwebappapi.Exceptions.InvalidFieldException;
import com.nobita.springboot.bankwebappapi.models.Customer;
import com.nobita.springboot.bankwebappapi.models.dto.AddCustomerDTO;
import com.nobita.springboot.bankwebappapi.services.CustomerService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CustomerController {
    private CustomerService service;
    private ModelMapper modelMapper;

    public CustomerController(CustomerService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) throws CustomerNotFoundException {
        Optional<Customer> customer = service.findByCustomerId(id);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        else {
            return ResponseEntity.ok(customer.get());
        }
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody AddCustomerDTO customer) {
        Customer customer1 = modelMapper.map(customer, Customer.class);
        Customer addedCustomer = service.addCustomer(customer1);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedCustomer.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> editCustomer
            (@PathVariable int id, @Valid @RequestBody AddCustomerDTO customer) throws CustomerNotFoundException
    {
        Optional<Customer> customer1 = service.findByCustomerId(id);
        if (customer1.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        else {
            Customer existingCustomer = customer1.get();
            existingCustomer.setName(customer.getName());
            existingCustomer.setPanNum(customer.getPanNum());

            service.addCustomer(existingCustomer);
            return ResponseEntity.ok(existingCustomer);
        }
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer
            (@PathVariable int id) throws CustomerNotFoundException
    {
        Optional<Customer> customer1 = service.findByCustomerId(id);
        if (customer1.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        else {
            service.deleteCustomer(id);
        }
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Customer> editCustomerFields(@PathVariable int id, @RequestBody Map<String, Object> fields) throws CustomerNotFoundException, InvalidFieldException {
        try {
            return ResponseEntity.ok(service.updateCustomerByFields(id, fields));
        }catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException();
        }catch (InvalidFieldException ex) {
            throw new InvalidFieldException(ex.getMessage());
        }
    }
}
