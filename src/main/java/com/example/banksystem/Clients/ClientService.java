package com.example.banksystem.Clients;

import com.example.banksystem.Auth.*;
import com.example.banksystem.Common.Enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthResponse createUser(ClientDto userDto) {

        if (userRepo.findByEmail(userDto.getUser().getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "User already exists with this email.");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.getUser().getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getUser().getPassword()));
        userEntity.setGender(userDto.getUser().getGender());
        userEntity.setMaritalStatus(userDto.getUser().getMaritalStatus());
        userEntity.setAddress(userDto.getUser().getAddress());
        userEntity.setDateOfBirth(userDto.getUser().getDateOfBirth());
        userEntity.setPinCode(passwordEncoder.encode(userDto.getUser().getPinCode()));
        userEntity.setNationalId(userDto.getUser().getNationalId());
        userEntity.setRole(Roles.USER);
        userEntity.setFirstName(userDto.getUser().getFirstName());
        userEntity.setLastName(userDto.getUser().getLastName());
        userEntity.setPhoneNumber(String.valueOf(userDto.getUser().getPhoneNumber()));

        UserEntity savedUser = userRepo.save(userEntity);

        CustomUserDetails userDetails = new CustomUserDetails(
            userEntity
        );
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, savedUser.getRole().name());
    }


    public AuthResponse loginWithPin(ClientDto userDto) {
        // 1. نبحث بالمُعرّف (إيميل أو هاتف)
        UserEntity user = userRepo.findByEmail(userDto.getUser().getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2. نتحقق من الـ PIN
        if (!passwordEncoder.matches(userDto.getUser().getPinCode(), user.getPinCode())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid PIN");
        }

        // 3. توليد الـ JWT
        CustomUserDetails userDetails = new CustomUserDetails(
               user
        );

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, user.getRole().name());
    }

    public String forgetPinCode(NewPincode request) {
        UserEntity user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (request.getNewPinCode() == null || request.getNewPinCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New PIN code cannot be empty");
        }

        // تشفير الـ PIN الجديد
        user.setPinCode(passwordEncoder.encode(request.getNewPinCode()));

        userRepo.save(user);

        return "Changed PIN code successfully";
    }


}
