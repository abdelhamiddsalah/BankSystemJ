package com.example.banksystem.Admin;

import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/admin/allEmployers")
    public ResponseEntity<List<EmplyerEntity>> getAllEmployers(){
        return   adminService.getAllEmplyers();
    }

    @DeleteMapping("/admin/employer/{id}")
    public String deleteEmployer(@PathVariable Long id){
        return adminService.deleteEmplyer(id);
    }

    @DeleteMapping("/admin/user/{id}")
    public String deleteUser(@PathVariable Long id){
        return adminService.deleteUser(id);
    }
}
