package com.example.banksystem.Auth;

import com.example.banksystem.Common.Enums.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Roles role;
    private String password;
    private String phoneNumber;
    private String nationalId;
    private Date dateOfBirth;
    @Embedded
    private AddressEntity address;
    @Enumerated(EnumType.STRING)
    private GenderEnums gender;
    private String  MaritalStatus;
}
