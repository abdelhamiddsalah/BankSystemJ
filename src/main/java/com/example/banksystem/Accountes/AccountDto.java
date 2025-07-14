package com.example.banksystem.Accountes;

import com.example.banksystem.Auth.UserEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    //@Enumerated(EnumType.STRING)
    private AccountesTypes accountType;
    private String accountNumber;
    private double balance;
    private LocalDateTime createdAt;
}
