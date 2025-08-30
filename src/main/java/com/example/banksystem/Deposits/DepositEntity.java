package com.example.banksystem.Deposits;


import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Auth.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deposits")
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositId;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference // 👈 تمنع Jackson من إعادة قراءة الـ user في كل مرة
    private UserEntity user;



    private LocalDateTime date;

    private String status;

    private String message;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id") // 👈 يفضل تحديد referencedColumnName
    private AccountEntity account;





}
