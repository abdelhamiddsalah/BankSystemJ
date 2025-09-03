package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pdf_files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    @Lob
    private byte[] data;

    @ManyToOne
    private UserEntity user;
    private String path; // ✅ رابط الملف

    @ManyToOne
    private EmplyerEntity employer;
}
