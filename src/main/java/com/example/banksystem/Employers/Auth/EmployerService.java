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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

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
        userEntity = userRepo.save(userEntity);  // دلوقتي واخد ID

// 2. إنشاء EmployerEntity وربطه بالـ UserEntity
        EmplyerEntity emplyerEntity = new EmplyerEntity();
        emplyerEntity.setUser(userEntity);
        userEntity.setEmployer(emplyerEntity);

        emplyerEntity.setJobTitle(employerDto.getJobTitle());
        emplyerEntity.setWorkBranch(employerDto.getWorkBranch());
        emplyerEntity.setDateOfhiring(employerDto.getDateOfhiring());
        emplyerEntity.setDepartment(employerDto.getDepartment());
        emplyerEntity.setEmplyeeID(copounEntity.getCopoun());

// ربط الـ CV
        CVEntity cv = employerDto.getCvee();
        cv.setEmployer(emplyerEntity);
        emplyerEntity.setCv(cv);

// الحفظ
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));


        // 2. نجيب القيمتين
        String enteredPin = employerDto.getUser().getPassword();   // اللي المستخدم دخلها
        String storedPin = emplyerEntity.getUser().getPassword();  // اللي في الداتا بيز (مشفر)

        // 3. نتحقق باستخدام PasswordEncoder
        if (!passwordEncoder.matches(enteredPin, storedPin)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Password");
        }

        // 4. لو صح، نكمل تسجيل الدخول
        CustomUserDetails userDetails = new CustomUserDetails(
                emplyerEntity.getUser()   // هنا بتمرر UserEntity اللي مربوط بـ Employer

        );

        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token, emplyerEntity.getUser().getRole().name());
    }

    public ResponseEntity<?> forgetPassword(EmployerDto employerDto) {
        EmplyerEntity emp = employerRepo.findByUser_Email(employerDto.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        // خزّن التوكن عند المدرس
        emp.setResetToken(token);
        employerRepo.save(emp);
        String resetLink = "http://localhost:8080/api/Employer/reset-password-form?token=" + token;
        emailService.sendPasswordResetEmail(employerDto.getUser().getEmail(), resetLink);

        return ResponseEntity.ok("Password reset link has been sent successfully to your email.");
    }

    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        // هنا محتاج تربط التوكن باليوزر
        // في البداية ممكن تخزن التوكن في Map أو DB مع الـ email

        EmplyerEntity emp = employerRepo.findByResetToken(token) // مثال
                .orElseThrow(() -> new RuntimeException("token not found"));

        // غير الباسورد
        emp.getUser().setPassword(passwordEncoder.encode(newPassword));

        return ResponseEntity.ok("Password has been reset successfully");
    }

}
