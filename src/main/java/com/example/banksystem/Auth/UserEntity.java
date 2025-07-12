package com.example.banksystem.Auth;
import com.example.banksystem.Common.Enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private Roles role;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "national_id")
    private String nationalId;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Embedded
    private AddressEntity address;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnums gender;
    @Column(name = "marital_status")
    private String  MaritalStatus;
}
