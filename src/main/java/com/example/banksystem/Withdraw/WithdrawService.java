package com.example.banksystem.Withdraw;

import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Accountes.AccountsRepo;
import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WithdrawService {

    @Autowired
    private WithdrawRepo withdrawRepo;

    @Autowired
    private AccountsRepo accountsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private JwtService jwtService;

    public WithdrawResponseDto WithdrawAmount(WithdrawDto withdrawDto) {
        // 1. Get user ID from token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        // 2. Find user
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Find account
        AccountEntity account = accountsRepo.findById(user.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 4. Update balance
        double amountToWithdraw = withdrawDto.getAmount();
        if (amountToWithdraw > account.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amountToWithdraw);
        accountsRepo.save(account);

        // 5. Create withdraw entity
        WithdrawEntity withdrawEntity = new WithdrawEntity();
        withdrawEntity.setAmount(amountToWithdraw);
        withdrawEntity.setUser(user);
        withdrawEntity.setAccount(account);
        withdrawEntity.setDate(LocalDateTime.now());
        withdrawEntity.setStatus("PENDING");
        withdrawEntity.setMessage("Withdraw Successfully");

        // 6. Save entity
        WithdrawEntity saved = withdrawRepo.save(withdrawEntity);

        // 7. Return response DTO
        return new WithdrawResponseDto(
                saved.getAmount(),
                saved.getUser().getId(),
                saved.getId(),
                saved.getDate(),
                saved.getStatus(),
                saved.getMessage()

        );
    }
}
