package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfService {
    @Autowired
    public  PdfsRepo pdfsRepo;

    @Autowired
    public UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    public String uploadPdf(MultipartFile multipartFile) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        UserEntity user = userRepo.findById(userId).orElse(null);

        if (!multipartFile.getContentType().equals("application/pdf")) {
            throw new Exception("Only PDF files are allowed.");
        }

        PdfFileEntity entity = new PdfFileEntity();
        entity.setFileName(multipartFile.getOriginalFilename());
        entity.setContentType(multipartFile.getContentType());
        entity.setData(multipartFile.getBytes());
        entity.setUser(user);

        pdfsRepo.save(entity);
        return "File uploaded successfully with ID: " + entity.getId();
    }

    public List<PdfDto> findAll(){
        return pdfsRepo.findAll()
                .stream()
                .map(pdf -> new PdfDto(
                        pdf.getId(),
                        pdf.getFileName(),
                        pdf.getContentType(),
                        "http://localhost:8080/api/uploadpdf/view/" + pdf.getId()
                ))
                .collect(Collectors.toList());
    }

    public PdfFileEntity getPdfById(Long id) {
        return pdfsRepo.findById(id).orElseThrow(() -> new RuntimeException("PDF not found"));
    }

}
