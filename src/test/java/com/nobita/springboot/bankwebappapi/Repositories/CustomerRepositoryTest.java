package com.nobita.springboot.bankwebappapi.Repositories;

import com.nobita.springboot.bankwebappapi.models.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer(1, "Naruto", "JSKFK7663K");

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertEquals("Naruto", savedCustomer.getName());
    }

    @Test
    public void testFindCustomerById() {
        Customer customer = new Customer(1, "Naruto", "JSKFK7663K");

        customerRepository.save(customer);

        Customer exisitingCustomer = customerRepository.findById(customer.getCustomerId()).get();

        assertNotNull(exisitingCustomer);
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer(1, "Naruto", "JSKFK7663K");
        Customer customer1 = customerRepository.save(customer);

        customerRepository.deleteById(customer1.getCustomerId());

        assertEquals(Optional.empty(), customerRepository.findById(customer1.getCustomerId()));
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer(1, "Naruto", "JSKFK7663K");
        Customer customer1 = customerRepository.save(customer);

        customer1.setName("Nobita");
        customer1.setPanNum("HFKFK7668K");

        Customer updatedCustomer = customerRepository.save(customer1);
        Optional<Customer> existedCustomer = customerRepository.findById(updatedCustomer.getCustomerId());

        assertNotNull(existedCustomer);
        assertEquals("Nobita", updatedCustomer.getName());
        assertEquals("HFKFK7668K", updatedCustomer.getPanNum());
    }
}