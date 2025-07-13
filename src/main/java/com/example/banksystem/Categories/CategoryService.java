package com.example.banksystem.Categories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    List<CategoryEntity> findAllCategories() {
        return categoryRepo.findAll();
    }
}
