package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.*;
import com.example.banksystem.Auth.CustomUserDetails;
import com.example.banksystem.Common.Enums.Roles;
import com.example.banksystem.Copouns.CopounEntity;
import com.example.banksystem.Copouns.CopounsRepo;
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

    @Autowired
    private CopounsRepo  copounsRepo;

    public EmployerAuthResponse sinup(EmployerDto employerDto) {
        // التأكد من أن البريد غير مستخدم
        if (employerRepo.findByEmail(employerDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Employee already exists with this email.");
        }

        // البحث عن الكوبون
        CopounEntity copounEntity = copounsRepo.findByCopoun(employerDto.getEmplyeeID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Copoun Not Found"));

        if (copounEntity.getCopoun() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Copoun Not Found");
        }


        if (copounEntity.isUsed() && copounEntity.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Copoun Expired");
        }

        // تحويل DTO إلى Entity
        EmplyerEntity emplyerEntity = employerMapper.emplyerEntity(employerDto);

        // تعيين الدور الافتراضي إذا لم يتم تحديده
        if (emplyerEntity.getRole() == null) {
            emplyerEntity.setRole(Roles.EMPLOYER);
        }

        // حفظ الـ Employer
        EmplyerEntity savedEmp = employerRepo.save(emplyerEntity);

        // تحديث حالة الكوبون وحفظها في قاعدة البيانات
        copounEntity.setUsed(true);
        copounEntity.setExpired(true);
        copounsRepo.save(copounEntity); // <--- مهم جداً

        // بناء UserDetails لتوليد التوكن
        UserDetails userDetails = new CustomUserDetails(
                savedEmp.getId(),
                savedEmp.getEmail(),
                savedEmp.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedEmp.getRole().name())),
                savedEmp.getPincode()
        );

        // توليد التوكن
        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token, savedEmp.getRole().name());
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
