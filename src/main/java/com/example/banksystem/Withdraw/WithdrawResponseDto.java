package com.example.banksystem.Withdraw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResponseDto {
    private double amount;
    private Long userId;
    private Long iidd;

    private LocalDateTime date;

    private String status;

    private String message;
}
