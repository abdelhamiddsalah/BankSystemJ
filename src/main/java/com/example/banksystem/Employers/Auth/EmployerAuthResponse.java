package com.example.banksystem.Employers.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployerAuthResponse {
    private String token;
    private String role;
}
