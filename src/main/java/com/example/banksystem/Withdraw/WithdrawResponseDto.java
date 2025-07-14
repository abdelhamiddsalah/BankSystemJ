package com.example.banksystem.Withdraw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResponseDto {
    private double amount;
    private String userEmail;
    private String accountNumber;
}
