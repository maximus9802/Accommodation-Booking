package com.quyvx.accommodationbooking.controller;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.model.Account;
import com.quyvx.accommodationbooking.service.account.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public String createAccount(@Valid @RequestBody Account account){
        return accountService.save(account);
    }

    @PutMapping("/update-information/{id}")
    public String updateAccount(@PathVariable("id") Long id, @RequestBody AccountDto accountDto){
        Account account = accountService.findById(id);
        account.setName(accountDto.getName());
        account.setAddress(accountDto.getAddress());
        account.setPhone(accountDto.getPhone());
        accountService.save(account);
        return "Successfully updated account information";
    }

    @GetMapping("/information/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") Long id){
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

}
