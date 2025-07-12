package com.example.banksystem.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse createUser(UserDto userDto) {

        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "User already exists with this email.");
        }
        // تحويل من DTO إلى Entity
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setRole(userDto.getRole());

        // تشفير الباسورد
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // حفظ المستخدم
        UserEntity savedUser = userRepo.save(userEntity);


        // بناء UserDetails لتوليد التوكن
        UserDetails userDetails = new CustomUserDetails(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name()))
        );


        // توليد التوكن
        String token = jwtService.generateToken(userDetails);

        // رجّع التوكن في response
        return new AuthResponse(token,  savedUser.getRole().name());
    }
}
