package com.example.banksystem.Admin;

import com.example.banksystem.Auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;

    public String registerAsAdmin(AdminEntity adminEntity) {
        if ("admin11@gmail.com".equals(adminEntity.getEmail()) &&
                "adminadmin".equals(adminEntity.getPassword())) {
            return "success";
        }
        return "fail";
    }
}

