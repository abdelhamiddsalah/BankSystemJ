package com.example.banksystem.Withdraw;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Withdraw")
public class WithdrawController {
    @Autowired
   private   WithdrawService withdrawService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public WithdrawResponseDto WithdrawAmount(@RequestBody WithdrawDto withdrawDto){
        return withdrawService.WithdrawAmount(withdrawDto);
    }
}
