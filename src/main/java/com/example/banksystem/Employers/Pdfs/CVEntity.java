package com.example.banksystem.Employers.Pdfs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "CVS")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CVEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String file;

    private String resultCv;
}
