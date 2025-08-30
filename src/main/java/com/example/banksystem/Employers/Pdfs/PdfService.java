package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Admin.AdminEnums;
import com.example.banksystem.Auth.JwtService;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Auth.UserRepo;
import com.example.banksystem.Employers.Auth.EmployerRepo;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public String uploadPdf(MultipartFile multipartFile) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();
        Long userId = jwtService.extractId(token);

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //EmplyerEntity emp = user.getEmployer();
       // if (emp == null) {
           // throw new RuntimeException("This user is not assigned to any employer.");
      //  }

        if (!multipartFile.getContentType().equals("application/pdf")) {
            throw new Exception("Only PDF files are allowed.");
        }

        PdfFileEntity entity = new PdfFileEntity();
        entity.setFileName(multipartFile.getOriginalFilename());
        entity.setContentType(multipartFile.getContentType());
        entity.setData(multipartFile.getBytes());
        entity.setUser(user);
     //   entity.setEmployer(user.getEmployer());

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
                        "http://localhost:8080/api/uploadpdf/view/" + pdf.getId(),
                        pdf.getEmployer().getEmplyeeID()
                ))
                .collect(Collectors.toList());
    }

    public PdfFileEntity getPdfById(Long id) {
        return pdfsRepo.findById(id).orElseThrow(() -> new RuntimeException("PDF not found"));
    }

    public String uploadCV(MultipartFile multipartFile) throws Exception {
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
        String uploadDir = "uploads/";

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
        cvEntity.setFile(uploadDir + fileName); // أو بس fileName لو مش عايز الـfull path
        cvEntity.setResultCv(AdminEnums.Waiting.toString());
        cvRepo.save(cvEntity);

        return uploadDir + fileName;
    }


    public List<CVEntity> getAllCVs() {
        return cvRepo.findAll();
    }

    public CVEntity getCVById(Long id) {
        return cvRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("CV not found"));
    }

    public CVEntity updateCVResult(Long id, String result, String copoun,double salary) {
        CVEntity cv = getCVById(id);

        // Normalize result
        String status = result.trim().toLowerCase();
        if (!status.equals("approved") && !status.equals("rejected")) {
            status = "waiting";
        }

        cv.setResultCv(status);
        // التعامل مع الكوبون
        if (status.equals("approved")) {
            cv.setCopoun(copoun); // حط الكوبون بس لو approved
            cv.setSalary(salary);
        } else {
            cv.setCopoun(null); // لو rejected أو waiting ما يكونش فيه كوبون
            cv.setSalary(0.0);
        }

        return cvRepo.save(cv);
    }

}
