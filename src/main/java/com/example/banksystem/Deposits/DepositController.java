package com.example.banksystem.Deposits;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return depositService.createDeposit(depositdto); // ✅ كده تمام
    }

    @GetMapping("/all")
   // @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<DepositEntity>> getAllDeposits() {
        return depositService.GetAllDeposits();
    }
}
