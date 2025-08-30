package com.example.banksystem.Deposits;

import com.example.banksystem.Accountes.AccountEntity;
import com.example.banksystem.Auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepositRepo extends JpaRepository<DepositEntity,Long> {
    public DepositEntity findByDepositId(Long depositId);
    public UserEntity findByUserId(Long userId);
    Optional<DepositEntity> findTopByAccountOrderByDateDesc(AccountEntity account);

}
