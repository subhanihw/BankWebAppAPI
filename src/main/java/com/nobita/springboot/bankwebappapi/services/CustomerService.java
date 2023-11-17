package com.nobita.springboot.bankwebappapi.services;

import com.nobita.springboot.bankwebappapi.Exceptions.CustomerNotFoundException;
import com.nobita.springboot.bankwebappapi.Exceptions.InvalidFieldException;
import com.nobita.springboot.bankwebappapi.Repositories.CustomerRepository;
import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository repository;
    private final AccountService accountService;

    public CustomerService(CustomerRepository repository, AccountService service) {
        this.repository = repository;
        this.accountService = service;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Optional<Customer> findByCustomerId(int id) {
        return repository.findById(id);
    }


    public Customer addCustomer(Customer customer) {
        return repository.save(customer);
    }

    public void deleteCustomer(int id) {
        Customer customer = findByCustomerId(id).get();
        List<Account> accountList = accountService.getAllAccountsByCustomerId(customer);
        for (Account account:accountList) {
            accountService.deleteAccount(account);
        }
        repository.deleteById(id);
    }

    public Customer updateCustomerByFields(int id, Map<String, Object> fields) throws CustomerNotFoundException, InvalidFieldException {
        Optional<Customer> existingCustomer = repository.findById(id);

        if (existingCustomer.isPresent())
        {
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Field field = ReflectionUtils.findField(Customer.class, key);
                if (field == null) {
                    throw new InvalidFieldException("Invalid Field Name");
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingCustomer.get(), value);
            }
            return repository.save(existingCustomer.get());
        }
        throw new CustomerNotFoundException(String.format("Customer with id = %s is not found", id));
    }

}
