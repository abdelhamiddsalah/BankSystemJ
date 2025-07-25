package com.example.banksystem.Accountes;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
   // @PreAuthorize("hasRole( 'USER')") // جرب تسمح للجميع
    public AccountDto createAccount( @RequestBody AccountDto accountRequest) {
        return accountService.createAccount(accountRequest);
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        return accountService.getBalance();
    }
}

