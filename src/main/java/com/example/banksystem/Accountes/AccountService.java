package com.example.banksystem.Accountes;
import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountsRepo accountsRepo;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;
    private final UserRepo   userRepo;

    public AccountDto createAccount(AccountDto accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        if (accountsRepo.existsByUserId(userId)) {
            AccountEntity existingAccount = accountsRepo.findByUserId(userId).orElseThrow();
            return accountMapper.toDto(existingAccount);
        }

        UserEntity user = userRepo.findById(userId).orElseThrow();
        AccountEntity entity = accountMapper.toEntity(accountRequest);

        entity.setAccountNumber(generateAccountNumber());
        entity.setAccountType(AccountesTypes.fromValue(String.valueOf(accountRequest.getAccountType())));
        entity.setBalance(0.0);
        entity.setCreatedAt(TimeUtil.getCurrentTime());
        entity.setUser(user); // فقط اربط الحساب بالمستخدم

        AccountEntity saved = accountsRepo.save(entity); // Hibernate هيعرف يربطهم صح
        return accountMapper.toDto(saved);
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
