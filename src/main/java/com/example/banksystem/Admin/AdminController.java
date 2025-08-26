package com.example.banksystem.Admin;

import com.example.banksystem.Auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/registerAdmin")
    public String RegusterAsAdmin(@RequestBody AdminEntity adminEntity){
      return    adminService.registerAsAdmin(adminEntity);
    }

    @GetMapping("/admin/allUsers")
    public List<UserEntity> getAllUsers(){
      return   adminService.getAllusers();
    }
}
