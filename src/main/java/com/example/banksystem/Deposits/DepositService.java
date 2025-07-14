package com.example.banksystem.Deposits;

import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepositService {

    @Autowired
    private DepositRepo depositRepo;

    @Autowired
    private DepositMapper depositMapper;

    @Autowired
    private UserRepo userRepo; // أضف هذا

    public DepositEntity CreateDeposit(DepositDto depositdto) {
        // استخرج الإيميل من التوكن
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // هات المستخدم من قاعدة البيانات
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // حوّل الـ DTO إلى Entity
        DepositEntity depositEntity = depositMapper.depositentity(depositdto);
        depositEntity.setAmount(depositdto.getAmount());
        depositEntity.setDepositId(depositdto.getDepositId());
        depositEntity.setStatus("PENDING");
        depositEntity.setDate(LocalDateTime.now());
        depositEntity.setMessage("Deposited Successfully");

        // اربطه بالمستخدم
        depositEntity.setUser(user);

        return depositRepo.save(depositEntity);
    }
}
