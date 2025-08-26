package com.example.banksystem.Admin;

import com.example.banksystem.Auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUsersRepo extends JpaRepository<UserEntity,Long> {
}
