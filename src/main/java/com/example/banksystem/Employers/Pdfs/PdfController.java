package com.example.banksystem.Employers.Pdfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
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

    @PostMapping("/upload-cv")
    public String uploadCV(@RequestParam("file") MultipartFile file) throws Exception {
        return pdfService.uploadCV(file);
    }


    @GetMapping("/admin/cvs")
    public List<CVEntity> getAllCVs() {
        return pdfService.getAllCVs();
    }

    // عرض CV معين
    @GetMapping("/cv/{id}")
    public CVEntity getCV(@PathVariable Long id) {
        return pdfService.getCVById(id);
    }

    @PutMapping("/admin/cv/{id}/update")
    public CVEntity updateCVResult(
            @PathVariable Long id,
            @RequestParam String result,
            @RequestParam(required = false) String copoun // اختياري
    ) {
        // Normalize value
        String status = result.trim().toLowerCase();

        if (!status.equals("approved") && !status.equals("rejected")) {
            status = "waiting"; // القيمة الافتراضية
        }

        return pdfService.updateCVResult(id, status, copoun);
    }


}
