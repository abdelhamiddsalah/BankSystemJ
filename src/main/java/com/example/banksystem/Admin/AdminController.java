package com.example.banksystem.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/registerAdmin")
    public String RegusterAsAdmin(@RequestBody AdminEntity adminEntity){
      return    adminService.registerAsAdmin(adminEntity);

    }
}
