package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.*;
import com.example.banksystem.Auth.CustomUserDetails;
import com.example.banksystem.Common.Enums.Roles;
import com.example.banksystem.Copouns.CopounEntity;
import com.example.banksystem.Copouns.CopounsRepo;
import com.example.banksystem.Employers.Pdfs.CVEntity;
import com.example.banksystem.Employers.Pdfs.CVRepo;
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
    private CopounsRepo  copounsRepo;

    public EmployerAuthResponse sinup(EmployerDto employerDto) {
        if (employerRepo.findByUser_Email(employerDto.getUser().getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Employee already exists with this email.");
        }

        if (employerDto.getCvee() == null || employerDto.getCvee().getCopoun() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Copoun is required");
        }

        // ✅ البحث عن الكوبون باستخدام الكوبون اللي في CV
        CopounEntity copounEntity = copounsRepo.findByCopoun(employerDto.getCvee().getCopoun())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Copoun Not Found"));

        if (copounEntity.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Copoun Already Used");
        }
        if (copounEntity.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Copoun Expired");
        }

        // 1. إنشاء UserEntity جديد
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(employerDto.getUser().getFirstName());
        userEntity.setLastName(employerDto.getUser().getLastName());
        userEntity.setEmail(employerDto.getUser().getEmail());
        userEntity.setPassword(passwordEncoder.encode(employerDto.getUser().getPassword()));
        userEntity.setRole(Roles.EMPLOYER);
        userEntity.setPinCode(passwordEncoder.encode(employerDto.getUser().getPinCode()));
        userEntity.setAddress(employerDto.getUser().getAddress());
        userEntity.setGender(employerDto.getUser().getGender());
        userEntity.setMaritalStatus(employerDto.getUser().getMaritalStatus());

// 2. إنشاء EmployerEntity وربطه بالـ UserEntity
        EmplyerEntity emplyerEntity = new EmplyerEntity();
        emplyerEntity.setUser(userEntity); // ✅ هنا لازم قبل أي save
        emplyerEntity.setJobTitle(employerDto.getJobTitle());
        emplyerEntity.setWorkBranch(employerDto.getWorkBranch());
        emplyerEntity.setDateOfhiring(employerDto.getDateOfhiring());
        emplyerEntity.setDepartment(employerDto.getDepartment());
        emplyerEntity.setEmplyeeID(copounEntity.getCopoun());

// 3. ربط الـ CV بالـ Employer
        CVEntity cv = employerDto.getCvee();
        cv.setEmployer(emplyerEntity);
        emplyerEntity.setCv(cv);

// 4. حفظ الـ Employer (مع الـ User بسبب الـ Cascade)
        EmplyerEntity savedEmp = employerRepo.save(emplyerEntity);


        // ✅ نعلم الكوبون إنه مستخدم
        copounEntity.setUsed(true);
        copounEntity.setExpired(true);
        copounsRepo.save(copounEntity);

        CustomUserDetails userDetails = new CustomUserDetails(
                savedEmp.getUser()   // هنا بتمرر UserEntity اللي مربوط بـ Employer
        );


        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token, savedEmp.getUser().getRole().name());
    }

    EmployerAuthResponse login(EmployerDto employerDto) {
        // 1. نجيب الـ Employer من الداتابيز
        EmplyerEntity emplyerEntity = employerRepo.findByUser_Email(employerDto.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        // 2. نجيب القيمتين
        String enteredPin = employerDto.getUser().getPinCode();   // اللي المستخدم دخلها
        String storedPin = emplyerEntity.getUser().getPinCode();  // اللي في الداتا بيز (مشفر)

        // 3. نتحقق باستخدام PasswordEncoder
        if (!passwordEncoder.matches(enteredPin, storedPin)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid PIN");
        }

        // 4. لو صح، نكمل تسجيل الدخول
        CustomUserDetails userDetails = new CustomUserDetails(
                emplyerEntity.getUser()   // هنا بتمرر UserEntity اللي مربوط بـ Employer

        );

        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token, emplyerEntity.getUser().getRole().name());
    }

}
