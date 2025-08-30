package com.example.banksystem.Employers.Pdfs;

import com.example.banksystem.Employers.Auth.EmplyerEntity;
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

    private String resultCv; // approved, rejected, waiting

    private String copoun; // الحقول المتعلقة بالكوبون لو approved

    private Double salary;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private EmplyerEntity employer;
}
