package com.example.banksystem.Employers.Auth;
import com.example.banksystem.Auth.AddressEntity;
import com.example.banksystem.Auth.GenderEnums;
import com.example.banksystem.Auth.UserEntity;
import com.example.banksystem.Common.Enums.Roles;
import com.example.banksystem.Employers.Pdfs.CVEntity;
import com.example.banksystem.Employers.Pdfs.PdfFileEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Table(name = "employers")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmplyerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String nationalID;
    @Embedded
    private AddressEntity  address;
    private Date dateOfBirth;
    private String emplyeeID;
    private String JobTitle;
    private String Department;
    private Date dateOfhiring;
    private String WorkBranch;
    private String MaterialStatus;
    @Enumerated(EnumType.STRING)
    private GenderEnums  Gender;
    @Enumerated(EnumType.STRING)
    private Roles role;

   // @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
   // private List<UserEntity> users;

    private String pincode;


    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PdfFileEntity> pdfFile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CVEntity cv;
}
