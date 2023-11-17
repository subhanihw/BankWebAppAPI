package com.nobita.springboot.bankwebappapi.services;

import com.nobita.springboot.bankwebappapi.Exceptions.InvalidAccountTypeException;
import com.nobita.springboot.bankwebappapi.Repositories.AccountRepository;
import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "S1", 1000.0, "Savings", 2.5));
        accounts.add(new Account(2, "C1", 500.0, "Current", null));

        when(repository.findAll()).thenReturn(accounts);

        assertThat(accountService.getAllAccounts().size(), is(2));
        verify(repository, times(1)).findAll();
    }

    @Test
    public void addAccountValidAccountTest() throws InvalidAccountTypeException {
        Account account = new Account();
        account.setAccountType("Savings");

        when(repository.countByAccountType("Savings")).thenReturn(2);
        when(repository.save(account)).thenReturn(account);

        Account result = accountService.addAccount(account);

        assertThat(result.getAccountNumber(), is("S3"));
        verify(repository, times(1)).save(account);
    }

    @Test ()
    public void addAccountInvalidValidAccountTest() {
        Account account = new Account();
        account.setAccountType("Sings");

        assertThrows(InvalidAccountTypeException.class, () -> accountService.addAccount(account));
        verify(repository, never()).save(account);
    }


    @Test
    public void getAccountByAccountNumberExistingTest() {
        String accNum = "S1";

        Account account = new Account();
        account.setAccountNumber(accNum);

        when(repository.findAccountByAccountNumber(accNum)).thenReturn(Optional.of(account));

        Optional<Account> res = accountService.getAccountByAccountNumber(accNum);

        assertTrue(res.isPresent());
        assertThat(res, is(Optional.of(account)));
        verify(repository, times(1)).findAccountByAccountNumber(accNum);
    }

    @Test
    public void getAccountByAccountNumberNonExistingTest() {
        String accNum = "SS123";

        Account account = new Account();
        account.setAccountNumber(accNum);

        when(repository.findAccountByAccountNumber(accNum)).thenReturn(Optional.empty());

        Optional<Account> res = accountService.getAccountByAccountNumber(accNum);

        assertFalse(res.isPresent());
        verify(repository, times(1)).findAccountByAccountNumber(accNum);
    }

    @Test
    public void updateBalanceTest() {
        Account account = new Account();
        account.setBalance(1000);
        double newBalance = 2000;

        when(repository.save(account)).thenReturn(account);

        accountService.updateBalance(account, newBalance);
        assertThat(newBalance, is(account.getBalance()));
        verify(repository, times(1)).save(account);
    }

    @Test
    public void deleteAccountTest() {
        String accountNumber = "S123";

        Account account = new Account();
        account.setAccountNumber(accountNumber);

        accountService.deleteAccount(account);

        verify(repository, times(1)).delete(account);
    }

    @Test
    public void getCountOfAccountTypeTest() {
        String accountType = "Savings";
        int count = 5;

        when(repository.countByAccountType(accountType)).thenReturn(count);

        int result = accountService.getCountOfAccountType(accountType);

        assertThat(result, is(count));
        verify(repository, times(1)).countByAccountType(accountType);
    }

    @Test
    public void getAllAccountsByCustomerIdTest() {
        int customerId = 5;

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "S1", 100.0, "Savings", 2.5));
        accounts.add(new Account(2, "C1", 500.0, "Current", null));

        when(repository.getAccountsByCustomer(any())).thenReturn(accounts);

        List<Account> result = accountService.getAllAccountsByCustomerId(new Customer(customerId, "Subhani", "JKSPS7223J"));

        assertThat(result.size(), is(accounts.size()));
        verify(repository, times(1)).getAccountsByCustomer(any());
    }

    @Test
    public void isExistsAccountTypeExistingTest() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account(1, "S1", 100.0, "Savings", 2.5));
        accountList.add(new Account(2, "C1", 500.0, "Current", null));

        boolean exists = accountService.isExistsAccountType(accountList, "Savings");

        assertTrue(exists);
    }

    @Test
    public void isExistsAccountTypeNotExistTest() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account(1, "S1", 100.0, "Savings", 2.5));
        accountList.add(new Account(2, "C1", 500.0, "Current", null));

        boolean exists = accountService.isExistsAccountType(accountList, "Fixed Deposit");

        assertFalse(exists);
    }
}