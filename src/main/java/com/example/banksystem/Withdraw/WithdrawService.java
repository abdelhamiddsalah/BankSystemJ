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

@Service
public class WithdrawService {

    @Autowired
    private   WithdrawRepo withdrawRepo;


    @Autowired
    private AccountsRepo accountsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private  WithdrawMapper withdrawMapper;
    @Autowired
    private JwtService jwtService;

    public WithdrawResponseDto WithdrawAmount(WithdrawDto withdrawDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        AccountEntity account = accountsRepo.findById(user.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() - withdrawDto.getAmount());
        accountsRepo.save(account);

        WithdrawEntity withdrawEntity = new WithdrawEntity();
        withdrawEntity.setAmount(withdrawDto.getAmount());
        withdrawEntity.setUser(user);
        withdrawEntity.setAccount(account);

        WithdrawEntity saved = withdrawRepo.save(withdrawEntity);

        return new WithdrawResponseDto(
                saved.getAmount(),
                saved.getUser().getEmail(),
                saved.getAccount().getAccountNumber()
        );
    }

}
