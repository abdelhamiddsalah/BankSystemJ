package com.example.banksystem.Employers.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/employee/")
public class EmployerController {
    @Autowired
    private EmployerService employerService;

    @PostMapping("signup")
    public EmployerAuthResponse signup(@RequestBody EmployerDto employerDto) {
        return employerService.sinup(employerDto);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody EmployerDto employerDto) {
        return ResponseEntity.ok(employerService.login(employerDto));
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody EmployerDto employerDto) {
        return ResponseEntity.ok(employerService.forgetPassword(employerDto));
    }
}
