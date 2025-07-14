package com.example.banksystem.Deposits;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDto {

        private Long depositId;
        private double amount;
        private String status;
        private LocalDateTime date;
        private Long userId;
        private String message;


}
