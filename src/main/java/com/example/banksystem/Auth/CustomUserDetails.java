// CustomUserDetails.java
package com.example.banksystem.Auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
   // private Long employerId;
    private String pincode;

    public CustomUserDetails(Long id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities,String pincode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
      //  this.employerId = employerId;
        this.pincode = pincode;
    }

    public Long getId() {
        return id;
    }

    //public Long getEmployerId() {
       // return employerId;
   // }

   // public void setEmployerId(Long employerId) {
      //  this.employerId = employerId;
   // }

    public String getPincode() {
        return pincode;
    }

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
