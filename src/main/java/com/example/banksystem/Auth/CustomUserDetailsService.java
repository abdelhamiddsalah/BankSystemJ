package com.example.banksystem.Auth;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = "ROLE_" + user.getRole().name();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),  // أو user.getFirstName() إذا بتسجل بالاسم
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
