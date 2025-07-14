package com.example.banksystem.Deposits;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepo extends JpaRepository<DepositEntity,Long> {
    public DepositEntity findByDepositId(Long depositId);
}
