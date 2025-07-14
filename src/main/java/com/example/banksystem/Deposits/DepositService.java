package com.example.banksystem.Deposits;

import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Accountes.AccountsRepo;
import com.example.banksystem.Auth.JwtService;
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
    private UserRepo userRepo;

    @Autowired
    private AccountsRepo accountsRepo;

    @Autowired
    private JwtService jwtService;

    public DepositEntity CreateDeposit(DepositDto depositdto) {
        // ✅ استخراج بيانات المصادقة
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long id = jwtService.extractId(token);
        String email = authentication.getName();

        // ✅ جلب المستخدم
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));



        // ✅ جلب الحساب المرتبط بالمستخدم
        AccountEntity account = accountsRepo.findById(user.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // ✅ تحديث الرصيد
        double depositAmount = depositdto.getAmount();
        account.setBalance(account.getBalance() + depositAmount);

        // ✅ حفظ التحديث في الحساب
        accountsRepo.save(account);

        // ✅ إنشاء كيان الإيداع
        DepositEntity depositEntity = depositMapper.depositentity(depositdto);
        depositEntity.setAmount(depositAmount);
        depositEntity.setDepositId(depositdto.getDepositId());
        depositEntity.setStatus("PENDING");
        depositEntity.setDate(LocalDateTime.now());
        depositEntity.setMessage("Deposited Successfully");

        // ✅ ربط الإيداع بالمستخدم
        depositEntity.setUser(user);

        return depositRepo.save(depositEntity);
    }
}
