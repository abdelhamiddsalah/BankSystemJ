package com.example.banksystem.Deposits;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    @Autowired
    private DepositService depositService;
    @Autowired
    private DepositMapper depositMapper;

    @PostMapping("/createDeposit")
    @PreAuthorize("hasRole('USER')")
    public DepositResponseDto createDeposit(@RequestBody DepositDto depositdto) {
        return depositService.CreateDeposit(depositdto); // ✅ كده تمام
    }


}
