package com.example.banksystem.Auth;

import com.example.banksystem.Common.Enums.Roles;
import com.example.banksystem.Employers.Auth.EmployerRepo;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
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
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setGender(userDto.getGender());
        userEntity.setMaritalStatus(userDto.getMaritalStatus());
        userEntity.setAddress(userDto.getAddress());
        userEntity.setDateOfBirth(userDto.getDateOfBirth());
        userEntity.setPinCode(passwordEncoder.encode(userDto.getPinCode()));
        userEntity.setNationalId(userDto.getNationalId());
        userEntity.setRole(Roles.USER);
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPhoneNumber(String.valueOf(userDto.getPhoneNumber()));


        if (userEntity.getRole() == null) {
            userEntity.setRole(Roles.USER);
        }

        UserEntity savedUser = userRepo.save(userEntity);

        UserDetails userDetails = new CustomUserDetails(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name())),
                savedUser.getPinCode()
        );
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, savedUser.getRole().name());
    }


    public AuthResponse loginWithPin(UserDto userDto) {
        // 1. نبحث بالمُعرّف (إيميل أو هاتف)
        UserEntity user = userRepo.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2. نتحقق من الـ PIN
        if (!passwordEncoder.matches(userDto.getPinCode(), user.getPinCode())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid PIN");
        }

        // 3. توليد الـ JWT
        UserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
            //    user.getEmployer() != null ? user.getEmployer().getId() : null,
                user.getPinCode()
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
