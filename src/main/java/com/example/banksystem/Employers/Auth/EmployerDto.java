package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.AddressEntity;
import com.example.banksystem.Auth.GenderEnums;
import com.example.banksystem.Common.Enums.Roles;
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
public class EmployerDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String nationalID;
    @Embedded
    private AddressEntity address;
    private Date dateOfBirth;
    private String emplyeeID;
    private String JobTitle;
    private String Department;
    private Date dateOfhiring;
    private String WorkBranch;
    private String MaterialStatus;
    @Enumerated(EnumType.STRING)
    private GenderEnums Gender;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
