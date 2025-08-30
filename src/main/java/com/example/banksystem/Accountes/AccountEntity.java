package com.example.banksystem.Accountes;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true) // 👈 لازم unique عشان OneToOne
    @JsonBackReference
    private UserEntity user;


   // @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
   // private List<DepositEntity> deposits;
}
