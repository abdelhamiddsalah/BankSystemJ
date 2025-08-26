package com.example.banksystem.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "admins")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity {
    private String email;
    private String password;
    @Id
    private Long id;

    private String result;
}
