package com.example.banksystem.Copouns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CopounsService {

    @Autowired
    private CopounsRepo copounsRepo; // ✅ نفس اسم Interface

    public CopounEntity addCopoun(CopounEntity copounEntity) {
        // تحقق لو الكوبون موجود
        Optional<CopounEntity> existingCopoun = copounsRepo.findByCopoun(copounEntity.getCopoun());
        if (existingCopoun.isPresent()) {
            throw new IllegalArgumentException("Coupon already exists!");
        }

        // تعيين القيم للكوبون الجديد
        copounEntity.setExpired(false);
        copounEntity.setUsed(false);

        // حفظ الكوبون الجديد فقط - لا تلمس الكوبونات القديمة
        return copounsRepo.save(copounEntity);
    }

    // إضافة methods مفيدة
    public List<CopounEntity> getActiveCoupons() {
        return copounsRepo.findByExpiredFalseAndUsedFalse();
    }

    public Optional<CopounEntity> getValidCopoun(String copounCode) {
        return copounsRepo.findByCopounAndExpiredFalseAndUsedFalse(copounCode);
    }

    public void useCopoun(String copounCode) {
        Optional<CopounEntity> copounOpt = copounsRepo.findByCopounAndExpiredFalseAndUsedFalse(copounCode);
        if (copounOpt.isPresent()) {
            CopounEntity copoun = copounOpt.get();
            copoun.setUsed(true);
            copounsRepo.save(copoun);
        } else {
            throw new IllegalArgumentException("Coupon not found or already used/expired");
        }
    }

    public void expireCopoun(String copounCode) {
        Optional<CopounEntity> copounOpt = copounsRepo.findByCopoun(copounCode);
        if (copounOpt.isPresent()) {
            CopounEntity copoun = copounOpt.get();
            copoun.setExpired(true);
            copounsRepo.save(copoun);
        } else {
            throw new IllegalArgumentException("Coupon not found");
        }
    }
}