package com.example.banksystem.Clients;

import com.example.banksystem.Auth.AuthResponse;
import com.example.banksystem.Auth.NewPincode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody ClientDto userDto)
    {
        return  ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody ClientDto userDto) {
        return userService.loginWithPin(userDto);
    }

    @PostMapping("/forgetpincode")
    public ResponseEntity<String> forgetPin(@RequestBody NewPincode request) {
        return ResponseEntity.ok(userService.forgetPinCode(request));
    }
}

