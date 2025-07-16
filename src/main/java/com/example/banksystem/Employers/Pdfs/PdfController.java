package com.example.banksystem.Employers.Pdfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/uploadpdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            String result = pdfService.uploadPdf(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload PDF: " + e.getMessage());
        }

    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<PdfDto> findAll(){
        return pdfService.findAll();
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewPdf(@PathVariable Long id) {
        PdfFileEntity pdf = pdfService.getPdfById(id); // هنضيفها في PdfService

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + pdf.getFileName() + "\"")
                .header("Content-Type", pdf.getContentType())
                .body(pdf.getData());
    }


}
