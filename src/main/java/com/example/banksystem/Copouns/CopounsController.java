package com.example.banksystem.Copouns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CopounsController {
    @Autowired
    private CopounsService copounsService;

    @PostMapping("/addcopoun")
    public CopounEntity AddCopoun(@RequestBody CopounEntity copounEntity) {
        return copounsService.addCopoun(copounEntity);
    }
}
