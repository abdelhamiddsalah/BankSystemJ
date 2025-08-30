package com.example.banksystem.Accountes;
import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class AccountService {

    @Autowired
    private  AccountsRepo accountsRepo;

    @Autowired

    private  JwtService jwtService;
    @Autowired

    private  UserRepo   userRepo;
    public AccountDto createAccount(AccountDto accountRequest) {
        // 🛡️ 1. جِب الـ userId من التوكن
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        // 🔍 2. تأكد إن المستخدم مش عامل حساب قبل كده
        if (accountsRepo.existsByUserId(userId)) {
            throw new IllegalStateException("User already has an account.");
        }

        // 👤 3. جيب بيانات المستخدم من الـ DB
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🏦 4. أنشئ AccountEntity جديد
        AccountEntity entity = new AccountEntity();
        entity.setBalance(accountRequest.getBalance() != 0.0 ? accountRequest.getBalance() : 0.0);
        entity.setAccountType(accountRequest.getAccountType());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUser(user);

        // 🎲 5. توليد رقم حساب لو مش موجود
        entity.setAccountNumber(
                accountRequest.getAccountNumber() != null
                        ? accountRequest.getAccountNumber()
                        : generateAccountNumber()
        );

        // 💾 6. خزّن البيانات
        AccountEntity saved = accountsRepo.save(entity);

        // 🔄 7. رجّع DTO يدوي بدون Mapper
        AccountDto response = new AccountDto();
        response.setId(saved.getId());
        response.setAccountType(saved.getAccountType());
        response.setAccountNumber(saved.getAccountNumber());
        response.setBalance(saved.getBalance());
        response.setCreatedAt(saved.getCreatedAt());
        response.setUserId(saved.getUser() != null ? saved.getUser().getId() : null);

        return response;
    }




    public BalanceResponse getBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        UserEntity user = userRepo.findById(userId).orElseThrow();
        AccountEntity entity = accountsRepo.findByUserId(userId).orElseThrow();

        return new BalanceResponse(entity.getBalance());
    }



    private String generateAccountNumber() {
        return "EG" + (long)(Math.random() * 1_000_000_000L);
    }

    public static class TimeUtil {
        public static LocalDateTime getCurrentTime() {
            return LocalDateTime.now();
        }
    }
}
