package com.example.client.controller.rest;

import com.example.client.model.AccountModel;
import com.example.client.model.ResponseModel;
import com.example.client.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountRestController {

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    private final AccountService accountService;

    @PostMapping("/account")
    public ResponseModel<Void> insertAndUpdateAccount(@RequestBody AccountModel accountModel) {
        return this.accountService.insertAccountAndUpdateThenResponse(accountModel);
    }

    @GetMapping("/account/{id}")
    public ResponseModel<AccountModel> findAccountById(@PathVariable("id") int id) {
        return this.accountService.findAccountIdThenResponse(id);
    }
}
