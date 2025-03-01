package com.example.service;

import org.h2.security.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.ClientErrorException;
import com.example.exception.ConflictException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public void register(Account account) throws ClientErrorException, ConflictException{
        if(account.getUsername() == ""){
            throw new ClientErrorException("username required");
        }
        if(account.getPassword().length() < 4){
            throw new ClientErrorException("password to short");
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new ConflictException(account.getUsername() + " already exists");
        }
        accountRepository.save(account);
    }

    public Account login(Account account) throws AuthenticationException{
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
            .orElseThrow(() -> new AuthenticationException("username or password incorrect"));
    }
}
