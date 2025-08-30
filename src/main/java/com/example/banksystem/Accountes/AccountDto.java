package com.example.banksystem.Accountes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private AccountesTypes accountType;
    private String accountNumber;
    private double balance;
    private LocalDateTime createdAt;
    private Long userId;

}
