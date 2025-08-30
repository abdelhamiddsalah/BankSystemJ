package com.example.banksystem.Auth;

import com.example.banksystem.Common.Enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Roles role;
    private String password;
    private String phoneNumber;
    private String nationalId;
    private Date dateOfBirth;
    private String pinCode;
    private AddressEntity address;
    private GenderEnums gender;
    private String maritalStatus;
   // private Long employerId; // ✅ موجود

    // إضافة explicit getters/setters للتأكد من أن MapStruct يراها
 //   public Long getEmployerId() {
     //   return employerId;
   // }

    //public void setEmployerId(Long employerId) {
    //    this.employerId = employerId;
   // }
}