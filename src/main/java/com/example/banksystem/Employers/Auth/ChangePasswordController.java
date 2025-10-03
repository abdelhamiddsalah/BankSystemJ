package com.example.banksystem.Employers.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/Employer")
public class ChangePasswordController {
        @Autowired
        private EmployerService employerService;

    @GetMapping("/reset-password-form")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password"; // This should map to reset-password.html in templates folder
    }


    @PostMapping("/reset-password")
    @ResponseBody // This tells Spring to return the response body directly, not a view
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword)
            {

        return employerService.resetPassword(token, newPassword);
    }

}
