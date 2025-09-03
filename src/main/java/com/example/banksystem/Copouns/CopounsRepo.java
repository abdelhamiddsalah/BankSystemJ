package com.example.banksystem.Copouns;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CopounsRepo extends JpaRepository<CopounEntity, Long> {
    List<CopounEntity> findByExpiredFalse();
    Optional<CopounEntity> findByCopoun(String copoun);

    // إضافات مفيدة
    Optional<CopounEntity> findByCopounAndExpiredFalseAndUsedFalse(String copoun);
    List<CopounEntity> findByExpiredFalseAndUsedFalse();
}