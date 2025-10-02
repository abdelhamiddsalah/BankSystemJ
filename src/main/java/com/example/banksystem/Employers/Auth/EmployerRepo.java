package com.example.banksystem.Employers.Auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepo extends JpaRepository<EmplyerEntity, Long> {
  Optional<EmplyerEntity> findByUser_Email(String email);

  Optional<EmplyerEntity> findById(Long id);
}
