package com.example.banksystem.Copouns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopounsService {
    @Autowired
    private CopounsRepo copounRepository;

    public CopounEntity addCopoun(CopounEntity copounEntity) {
        // تحقق لو الكوبون موجود
        if (copounRepository.findByCopoun(copounEntity.getCopoun()).isPresent()) {
            throw new IllegalArgumentException("Coupon already exists!");
        }

        // خلي كل الكوبونات القديمة Expired
        List<CopounEntity> oldCoupons = copounRepository.findAll();
        for (CopounEntity oldCoupon : oldCoupons) {
            oldCoupon.setExpired(true);
            oldCoupon.setUsed(true);
        }
        copounRepository.saveAll(oldCoupons);

        copounEntity.setExpired(false);
        copounEntity.setUsed(false);
        return copounRepository.save(copounEntity);
    }
}
