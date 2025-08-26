package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.*;
import com.example.banksystem.Auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class EmployerService {
    @Autowired
    private EmployerRepo employerRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
     private  PasswordEncoder passwordEncoder;
    @Autowired
    private EmployerMapper  employerMapper;

    public EmployerAuthResponse sinup(EmployerDto employerDto) {
        if (employerRepo.findByEmail(employerDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Employee already exists with this email.");
        }
      EmplyerEntity emplyerEntity = employerMapper.emplyerEntity(employerDto);

        EmplyerEntity savedEmp= employerRepo.save(emplyerEntity);

        // بناء UserDetails لتوليد التوكن
        UserDetails userDetails = new CustomUserDetails(
                savedEmp.getId(),
                savedEmp.getEmail(),
                savedEmp.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedEmp.getRole().name())),
               // savedEmp.getId(),
                savedEmp.getPincode()
        );
        // توليد التوكن
        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token,savedEmp.getRole().name());

    }

    EmployerAuthResponse login(EmployerDto employerDto) {
        // 1. نجيب الـ Employer من الداتابيز
        EmplyerEntity emplyerEntity = employerRepo.findByEmail(employerDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        // 2. نجيب القيمتين
        String enteredPin = employerDto.getPincode();   // اللي المستخدم دخلها
        String storedPin = emplyerEntity.getPincode();  // اللي في الداتا بيز (مشفر)

        // 3. نتحقق باستخدام PasswordEncoder
        if (!passwordEncoder.matches(enteredPin, storedPin)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid PIN");
        }

        // 4. لو صح، نكمل تسجيل الدخول
        UserDetails userDetails = new CustomUserDetails(
                emplyerEntity.getId(),
                emplyerEntity.getEmail(),
                emplyerEntity.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + emplyerEntity.getRole().name())),
                emplyerEntity.getPincode()
        );

        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token, emplyerEntity.getRole().name());
    }

}
