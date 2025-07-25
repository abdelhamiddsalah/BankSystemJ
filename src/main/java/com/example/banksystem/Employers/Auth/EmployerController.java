package com.example.banksystem.Employers.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee/signup")
public class EmployerController {
    @Autowired
    private EmployerService employerService;

    @PostMapping
    public EmployerAuthResponse signup(@RequestBody EmployerDto employerDto) {
        return employerService.sinup(employerDto);
    }
}
