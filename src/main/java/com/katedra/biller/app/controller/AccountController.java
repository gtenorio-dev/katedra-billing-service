package com.katedra.biller.app.controller;

import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/findAll")
    public ResponseEntity<List<AccountEntity>> getAll () {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody AccountEntity account) {
        accountService.createAccount(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
