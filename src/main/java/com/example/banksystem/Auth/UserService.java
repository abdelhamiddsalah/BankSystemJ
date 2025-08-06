package com.example.banksystem.Auth;

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
    private final EmployerRepo employerRepo;

    public AuthResponse createUser(UserDto userDto) {

        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "User already exists with this email.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setRole(userDto.getRole());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setDateOfBirth(userDto.getDateOfBirth());
        userEntity.setMaritalStatus(userDto.getMaritalStatus());
        userEntity.setGender(userDto.getGender());
        userEntity.setAddress(userDto.getAddress());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        userEntity.setNationalId(userDto.getNationalId());

        if (userDto.getEmplyerid() != null) {
            EmplyerEntity employer = employerRepo.findById(userDto.getEmplyerid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employer not found."));
            userEntity.setEmployer(employer);
        }


        // حفظ المستخدم
        UserEntity savedUser = userRepo.save(userEntity);
        UserDetails userDetails = new CustomUserDetails(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name())),
                savedUser.getEmployer() != null ? savedUser.getEmployer().getId() : null // ✅ الجديد
        );


        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getRole().name());
    }

}
