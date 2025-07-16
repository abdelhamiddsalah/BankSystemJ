package com.example.banksystem.Categories;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;


    @Cacheable("categories")
    public List<CategoryEntity> findAllCategories() {
        System.out.println("🔥 Fetching from DB...");
        return categoryRepo.findAll();
    }

}
