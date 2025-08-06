package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Employers.Auth.EmplyerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfDto {
    private Long id;

    private String fileName;

    private String contentType;
    private String viewUrl;

    private String employerid;
}
