package com.example.banksystem.Categories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/Categories")
    public List<CategoryEntity> findAllCategories() {
        return categoryService.findAllCategories();
    }
}
