package com.example.banksystem.Admin;

import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import com.example.banksystem.Copouns.CopounEntity;
import com.example.banksystem.Employers.Auth.EmployerDto;
import com.example.banksystem.Employers.Auth.EmployerRepo;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmployerRepo  employerRepo;

    public String registerAsAdmin(AdminEntity adminEntity) {
        if ("admin11@gmail.com".equals(adminEntity.getEmail()) &&
                "adminadmin".equals(adminEntity.getPassword())) {
            return "success";
        }
        return "fail";
    }

    public List<UserEntity> getAllusers() {
        return userRepo.findAll();
    }


    public ResponseEntity<List<EmplyerEntity>> getAllEmplyers() {
        List<EmplyerEntity> ems = employerRepo.findAll();
        if (ems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employers found");
        }
        return ResponseEntity.ok(ems);
    }

    public String deleteEmplyer(Long id) {
        EmplyerEntity em = employerRepo.findById(id).get();
        if (employerRepo.findById(em.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employer found");
        } else {
            employerRepo.deleteById(em.getId());
            return "success";
        }
    }

        public String deleteUser(Long id) {
            UserEntity usere = userRepo.findById(id).get();
            if (userRepo.findById(usere.getId()).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employer found");
            } else {
                userRepo.deleteById(usere.getId());
                return "success";
            }
        }

        public EmplyerEntity getEmplyer(Long id) {
        EmplyerEntity em = employerRepo.findById(id).get();
        if (employerRepo.findById(em.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employer found");
        }
        return em;
        }

    public List<EmplyerEntity> SearchEmplyer(String name) {
        if (name == null || name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name Not Found");
        }

        List<EmplyerEntity> ems = employerRepo.findAll();
        if (ems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employers found");
        }

        List<EmplyerEntity> filtered = ems.stream()
                .filter(e -> e.getUser().getFirstName() != null && e.getUser().getFirstName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name not found");
        }

        return filtered;
    }



}

