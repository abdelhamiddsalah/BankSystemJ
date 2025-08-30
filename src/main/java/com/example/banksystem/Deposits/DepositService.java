package com.example.banksystem.Deposits;

import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Accountes.AccountsRepo;
import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public DepositResponseDto createDeposit(DepositDto depositDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);
        String email = authentication.getName();

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccountEntity account = accountsRepo.findById(user.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double depositAmount = depositDto.getAmount();

        // تحديث رصيد الحساب
        account.setBalance(account.getBalance() + depositAmount);
        accountsRepo.save(account);

        // 🔹 البحث عن آخر إيداع للحساب
        Optional<DepositEntity> lastDepositOpt = depositRepo.findTopByAccountOrderByDateDesc(account);

        DepositEntity depositEntity;
        if (lastDepositOpt.isPresent()) {
            // تحديث آخر إيداع
            depositEntity = lastDepositOpt.get();
            depositEntity.setAmount(depositEntity.getAmount() + depositAmount);
            depositEntity.setDate(LocalDateTime.now());
            depositEntity.setStatus("PENDING");
            depositEntity.setMessage("Deposited Successfully");
        } else {
            // إنشاء إيداع جديد
            depositEntity = new DepositEntity();
            depositEntity.setAmount(depositAmount);
            depositEntity.setStatus("PENDING");
            depositEntity.setDate(LocalDateTime.now());
            depositEntity.setMessage("Deposited Successfully");
            depositEntity.setUser(user);
            depositEntity.setAccount(account);
        }

        DepositEntity saved = depositRepo.save(depositEntity);

        return new DepositResponseDto(
                saved.getDepositId(),
                saved.getAmount(),
                saved.getStatus(),
                saved.getDate(),
                saved.getUser().getId(),
                saved.getMessage()
        );
    }

    public ResponseEntity<List<DepositEntity>> GetAllDeposits() {
        List<DepositEntity> Depositslist = depositRepo.findAll();
        if (Depositslist.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(Depositslist, HttpStatus.OK);
        }
    }

}
