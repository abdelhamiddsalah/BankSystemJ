package com.example.banksystem.Auth;
import com.example.banksystem.Employers.Auth.EmployerRepo;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmployerRepo employerRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try user first
        var userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String role = "ROLE_" + user.getRole().name();
            GrantedAuthority authority = new SimpleGrantedAuthority(role);

            return new CustomUserDetails(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(authority),
                    user.getEmployer().getId(),
                    user.getPinCode()
            );
        }

        // Try employer
        var employerOpt = employerRepo.findByEmail(email);
        if (employerOpt.isPresent()) {
            EmplyerEntity employer = employerOpt.get();
            String role = "ROLE_" + employer.getRole().name();
            GrantedAuthority authority = new SimpleGrantedAuthority(role);

            return new CustomUserDetails(
                    employer.getId(),
                    employer.getEmail(),
                    employer.getPassword(),
                    Collections.singletonList(authority),
                    employer.getId(),
                    employer.getPincode()
            );
        }

        throw new UsernameNotFoundException("User or Employer not found with email: " + email);
    }

}
