package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Employers.Pdfs.CVEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDto {

    private Long id;

    @NotBlank(message = "Employee ID is required")
    private String emplyeeID;

    @NotBlank(message = "Job Title is required")
    @Size(min = 2, max = 50, message = "Job Title must be between 2 and 50 characters")
    private String JobTitle;

    @NotBlank(message = "Department is required")
    private String Department;

    @Past(message = "Hiring date must be in the past")
    private Date dateOfhiring;

    @NotBlank(message = "Work branch is required")
    private String WorkBranch;

    private CVEntity Cvee;

    @NotNull(message = "User must be provided")
    private UserEntity user;
}
