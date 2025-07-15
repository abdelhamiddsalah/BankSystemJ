package com.example.banksystem.Withdraw;

import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Auth.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawDto {

    private double amount;

    private UserEntity user;
    private AccountEntity account;
    private LocalDateTime date;

    private String status;

    private String message;

}
