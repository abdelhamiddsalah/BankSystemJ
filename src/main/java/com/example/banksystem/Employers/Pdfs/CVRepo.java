package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Copouns.CopounEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CVRepo extends JpaRepository<CVEntity, Long> {
    Optional<CVEntity> findByCopoun(String copoun);  // ✅ صح
}
