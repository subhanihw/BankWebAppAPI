package com.nobita.springboot.bankwebappapi.Repositories;

import com.nobita.springboot.bankwebappapi.models.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer(1, "Naurto", "JSKFK7663K");

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertEquals("Naurto", savedCustomer.getName());
    }

    @Test
    public void testFindCustomerById() {
        Customer customer = new Customer(1, "Naurto", "JSKFK7663K");

        customerRepository.save(customer);

        Customer exisitingCustomer = customerRepository.findById(customer.getCustomerId()).get();

        assertNotNull(exisitingCustomer);
    }
}