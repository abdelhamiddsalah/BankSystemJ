package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.*;
import com.example.banksystem.Auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmployerService {
    @Autowired
    private EmployerRepo employerRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
     private  PasswordEncoder passwordEncoder;

    public EmployerAuthResponse sinup(EmployerDto employerDto) {
        EmplyerEntity emplyerEntity = new EmplyerEntity();
        emplyerEntity.setFirstName(employerDto.getFirstName());
        emplyerEntity.setLastName(employerDto.getLastName());
        emplyerEntity.setEmail(employerDto.getEmail());
        emplyerEntity.setPassword(passwordEncoder.encode(employerDto.getPassword()));
        emplyerEntity.setAddress(employerDto.getAddress());
        emplyerEntity.setRole(employerDto.getRole());
        emplyerEntity.setDateOfBirth(employerDto.getDateOfBirth());
        emplyerEntity.setGender(employerDto.getGender());
        emplyerEntity.setEmplyeeID(employerDto.getEmplyeeID());
        emplyerEntity.setDepartment(employerDto.getDepartment());
        emplyerEntity.setDateOfhiring(employerDto.getDateOfhiring());
        emplyerEntity.setJobTitle(employerDto.getJobTitle());
        emplyerEntity.setWorkBranch(employerDto.getWorkBranch());

        EmplyerEntity savedEmp= employerRepo.save(emplyerEntity);

        // بناء UserDetails لتوليد التوكن
        UserDetails userDetails = new CustomUserDetails(
                savedEmp.getId(),
                savedEmp.getEmail(),
                savedEmp.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedEmp.getRole().name())),
                savedEmp.getId()
        );


        // توليد التوكن
        String token = jwtService.generateToken(userDetails);

        return new EmployerAuthResponse(token,savedEmp.getRole().name());

    }
}
