package com.example.banksystem.Accountes;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountsRepo accountsRepo;
    private final AccountMapper accountMapper;

    public AccountDto createAccount(AccountDto accountRequest) {
        AccountEntity entity = accountMapper.toEntity(accountRequest);
        entity.setAccountNumber(generateAccountNumber());
        entity.setAccountType(AccountesTypes.fromValue(String.valueOf(accountRequest.getAccountType())));

        entity.setBalance(0.0);
        entity.setCreatedAt(TimeUtil.getCurrentTime());

        AccountEntity saved = accountsRepo.save(entity);
        return accountMapper.toDto(saved);
    }

    private String generateAccountNumber() {
        return "EG" + (long)(Math.random() * 1_000_000_000L);
    }

    public class TimeUtil {
        public static LocalDateTime getCurrentTime() {
            return LocalDateTime.now();
        }
    }
}
