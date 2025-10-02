// CustomUserDetails.java
package com.example.banksystem.Auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
   private UserEntity user;
    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }



    public Long getId() {
        return user.getId();
    }
    public String getEmail() {
         return user.getEmail();
         }
    //public Long getEmployerId() {
       // return employerId;
   // }

   // public void setEmployerId(Long employerId) {
      //  this.employerId = employerId;
   // }

    public String getPincode() {
        return user.getPinCode();
    }

    @Override public String getUsername() {     return user.getEmail();
    }
    @Override public String getPassword() { return user.getPassword(); }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
