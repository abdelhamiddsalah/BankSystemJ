package com.example.banksystem.Copouns;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CopounsRepo extends JpaRepository<CopounEntity,Long> {
    List<CopounEntity> findByExpiredFalse();
    Optional<CopounEntity> findByCopoun(String copoun);
}
