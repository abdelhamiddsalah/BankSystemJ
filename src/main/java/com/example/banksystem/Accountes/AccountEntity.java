package com.example.banksystem.Accountes;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Convert(converter = AccountesTypesConverter.class)
    @Column(name = "account_type")
    private AccountesTypes accountType;
    
    private String accountNumber;


    private double balance;

    private LocalDateTime createdAt;

}
