package com.nobita.springboot.bankwebappapi.services;

import com.nobita.springboot.bankwebappapi.Exceptions.InvalidAccountTypeException;
import com.nobita.springboot.bankwebappapi.Repositories.AccountRepository;
import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public Account addAccount(Account account) throws InvalidAccountTypeException {

        String accountType = account.getAccountType();
        int count = getCountOfAccountType(accountType);

        String prefix;
        if (accountType.equalsIgnoreCase("Current")) {
            prefix = "C";
        } else if (accountType.equalsIgnoreCase("Savings")) {
            prefix = "S";
        }else  if (accountType.equalsIgnoreCase("Fixed Deposit")){
            prefix = "FD";
        }else {
            throw new InvalidAccountTypeException();
        }


        String accountNum = String.format("%s%d", prefix, count+1);
        account.setAccountNumber(accountNum);
        return repository.save(account);
    }

    public Optional<Account> getAccountByAccountNumber(String accountNum) {
        return repository.findAccountByAccountNumber(accountNum);
    }

    public void updateBalance(Account account, double balance) {
        account.setBalance(balance);
        repository.save(account);
    }

    public void deleteAccount(Account account) {
        repository.delete(account);
    }

    public int getCountOfAccountType(String type) {
        return repository.countByAccountType(type);
    }

    public List<Account> getAllAccountsByCustomerId(Customer customer) {
        return repository.getAccountsByCustomer(customer);
    }

    public boolean isExistsAccountType(List<Account> accountList, String accountType) {
        return accountList.stream().anyMatch(account -> account.getAccountType().equals(accountType));
    }
}
