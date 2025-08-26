package com.example.banksystem.Admin;

import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import com.example.banksystem.Copouns.CopounEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    public String registerAsAdmin(AdminEntity adminEntity) {
        if ("admin11@gmail.com".equals(adminEntity.getEmail()) &&
                "adminadmin".equals(adminEntity.getPassword())) {
            return "success";
        }
        return "fail";
    }

    public List<UserEntity> getAllusers() {
        return userRepo.findAll();
    }



}

