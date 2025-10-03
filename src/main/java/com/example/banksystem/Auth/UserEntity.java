package com.example.banksystem.Auth;
import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Common.Enums.GenderEnums;
import com.example.banksystem.Common.Enums.Roles;
import com.example.banksystem.Deposits.DepositEntity;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "national_id")
    private String nationalId;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "pin_code")
    private String pinCode;
    @Embedded
    private AddressEntity address;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnums gender;
    @Column(name = "marital_status")
    private String maritalStatus;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private EmplyerEntity employer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private AccountEntity account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DepositEntity> deposits;


}
