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
        if (employerRepo.findByEmail(employerDto.getEmail()).isPresent()) {
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

        EmplyerEntity emplyerEntity = new EmplyerEntity();
        emplyerEntity.setFirstName(employerDto.getFirstName());
        emplyerEntity.setLastName(employerDto.getLastName());
        emplyerEntity.setEmail(employerDto.getEmail());
        emplyerEntity.setPassword(passwordEncoder.encode(employerDto.getPassword()));
        emplyerEntity.setRole(Roles.EMPLOYER);
        emplyerEntity.setPincode(passwordEncoder.encode(employerDto.getPincode()));
        emplyerEntity.setJobTitle(employerDto.getJobTitle());
        emplyerEntity.setWorkBranch(employerDto.getWorkBranch());
        emplyerEntity.setDateOfhiring(employerDto.getDateOfhiring());
        emplyerEntity.setDepartment(employerDto.getDepartment());
        emplyerEntity.setEmplyeeID(copounEntity.getCopoun()); // ✅ ربط الكوبون
        emplyerEntity.setAddress(employerDto.getAddress());
        emplyerEntity.setGender(employerDto.getGender());
        emplyerEntity.setMaterialStatus(employerDto.getMaterialStatus());

        // ✅ ربط الـ CV بالـ Employer
        CVEntity cv = employerDto.getCvee();
        cv.setEmployer(emplyerEntity);
        emplyerEntity.setCv(cv);

        EmplyerEntity savedEmp = employerRepo.save(emplyerEntity);

        // ✅ نعلم الكوبون إنه مستخدم
        copounEntity.setUsed(true);
        copounEntity.setExpired(true);
        copounsRepo.save(copounEntity);

        UserDetails userDetails = new CustomUserDetails(
                savedEmp.getId(),
                savedEmp.getEmail(),
                savedEmp.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedEmp.getRole().name())),
                savedEmp.getPincode()
        );

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
