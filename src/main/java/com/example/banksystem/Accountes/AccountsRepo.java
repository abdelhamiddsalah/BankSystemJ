package com.example.banksystem.Accountes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepo extends JpaRepository<AccountEntity,Long> {
    boolean existsAccountEntityById(Long id);
    boolean existsByUserId(Long userId);
    Optional<AccountEntity> findByUserId(Long userId);


}
