package com.example.banksystem.Withdraw;

import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Auth.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawDto {

    private double amount;

    private UserEntity user;
    private AccountEntity account;

}
