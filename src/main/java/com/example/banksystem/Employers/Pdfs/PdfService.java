package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Admin.AdminEnums;
import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserRepo;
import com.example.banksystem.Copouns.CopounEntity;
import com.example.banksystem.Copouns.CopounsRepo;
import com.example.banksystem.Employers.Auth.EmployerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfService {
    @Autowired
    public  PdfRepo pdfsRepo;

    @Autowired
    public UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmployerRepo  employerRepo;

    @Autowired
    private CVRepo cvRepo;

    @Autowired
    private CopounsRepo copounrepo;

    public String uploadPdf(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new Exception("File is empty.");
        }

        if (!multipartFile.getContentType().equals("application/pdf")) {
            throw new Exception("Only PDF files are allowed.");
        }

        // ✅ اسم الملف
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

        // ✅ مسار الحفظ داخل static/uploads
        String uploadDir = new File("src/main/resources/static/uploads").getAbsolutePath();

        // ✅ تأكد أن الفولدر موجود
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // ✅ إنشاء الملف وحفظه
        File file = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new Exception("Error while saving the file.", e);
        }

        // ✅ هنا ممكن تحفظ في DB فقط اسم الملف (اختياري)
        PdfFileEntity entity = new PdfFileEntity();
        entity.setFileName(fileName);
        entity.setContentType(multipartFile.getContentType());
        entity.setPath("/uploads/" + fileName);
        pdfsRepo.save(entity);

        return "File uploaded successfully: /uploads/" + fileName;
    }



    public List<PdfDto> findAll() {
        return pdfsRepo.findAll()
                .stream()
                .map(pdf -> new PdfDto(
                        pdf.getId(),
                        pdf.getFileName(),
                        pdf.getContentType(),
                        "http://localhost:8080" + pdf.getPath(),
                        pdf.getEmployer() != null ? pdf.getEmployer().getEmplyeeID() : null
                ))
                .collect(Collectors.toList());
    }


    public PdfFileEntity getPdfById(Long id) {
        return pdfsRepo.findById(id).orElseThrow(() -> new RuntimeException("PDF not found"));
    }

    public CVResponse uploadCV(MultipartFile multipartFile) throws Exception {
        // ✅ تحقق من نوع الملف
        if (multipartFile.isEmpty()) {
            throw new Exception("File is empty.");
        }

        if (!"application/pdf".equals(multipartFile.getContentType())) {
            throw new Exception("Only PDF files are allowed.");
        }

        // ✅ اسم الملف
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

        // ✅ مسار الحفظ
        String uploadDir = "static/uploads/";

        // ✅ تأكد أن الفولدر موجود
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // ✅ إنشاء الملف وحفظه
        File file = new File(uploadDir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new Exception("Error while saving the file.", e);
        }

        // ✅ إنشاء CVEntity وحفظ البيانات
        CVEntity cvEntity = new CVEntity();
        cvEntity.setFile(uploadDir + fileName);
        cvEntity.setResultCv(AdminEnums.Waiting.toString());
        cvRepo.save(cvEntity);

        // ✅ إنشاء CVResponse وإرجاعه
        CVResponse response = new CVResponse();
        response.setId(cvEntity.getId()); // ⬅️ رجّع نفس الـ ID

        response.setCv(uploadDir + fileName);
        return response;
    }



    public List<CVEntity> getAllCVs() {
        return cvRepo.findAll();
    }

    public CVEntity getCVById(Long id) {
        return cvRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("CV not found"));
    }

    // ✅ Service
    public CVEntity updateCVResult(Long id, String result, String copoun, Double salary) {
        CVEntity cv = getCVById(id);

        // Normalize result
        String status = (result == null || result.isBlank()) ? "waiting" : result.trim().toLowerCase();
        if (!status.equals("approved") && !status.equals("rejected")) {
            status = "waiting";
        }
        cv.setResultCv(status);

        if ("approved".equals(status)) {
            // ✅ إنشاء كوبون جديد
            if (copoun != null && !copoun.isBlank()) {
                CopounEntity newCopoun = new CopounEntity();
                newCopoun.setCopoun(copoun);
                newCopoun.setUsed(false);
                newCopoun.setExpired(false);

                copounrepo.save(newCopoun); // حفظ الكوبون الجديد

                cv.setCopoun(copoun); // حفظ الكوبون في الـ CV
            }

            cv.setSalary(salary != null ? salary : 0.0);
        } else {
            cv.setCopoun(null);
            cv.setSalary(0.0);
        }

        return cvRepo.save(cv);
    }


}
