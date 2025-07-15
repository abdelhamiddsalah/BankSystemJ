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

    public DepositResponseDto CreateDeposit(DepositDto depositdto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long id = jwtService.extractId(token);
        String email = authentication.getName();

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccountEntity account = accountsRepo.findById(user.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double depositAmount = depositdto.getAmount();
        account.setBalance(account.getBalance() + depositAmount);
        accountsRepo.save(account);

        DepositEntity depositEntity = new DepositEntity();
        depositEntity.setAmount(depositAmount);
        depositEntity.setStatus("PENDING");
        depositEntity.setDate(LocalDateTime.now());
        depositEntity.setMessage("Deposited Successfully");
        depositEntity.setUser(user);
        depositEntity.setAccount(account);

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

}
