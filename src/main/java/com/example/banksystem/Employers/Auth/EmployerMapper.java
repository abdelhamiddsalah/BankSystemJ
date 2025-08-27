package com.example.banksystem.Employers.Auth;
import com.example.banksystem.Common.Enums.Roles;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class EmployerMapper {
     @Autowired
     protected PasswordEncoder passwordEncoder;

     // MapStruct هيتعامل مع باقي الحقول اللي ليها نفس الاسم تلقائياً
     abstract EmplyerEntity emplyerEntity(EmployerDto employerDto);
     abstract EmployerDto employerDto(EmplyerEntity emplyerEntity);

     @AfterMapping
     protected void afterMapping(EmployerDto dto, @MappingTarget EmplyerEntity entity) {
          // تشفير الباسوورد لو موجود
          if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
               entity.setPassword(passwordEncoder.encode(dto.getPassword()));
          }
          // تشفير الـ pincode لو موجود
          if (dto.getPincode() != null && !dto.getPincode().isEmpty()) {
               entity.setPincode(passwordEncoder.encode(dto.getPincode()));
          }
          // تعيين الدور الافتراضي لو مش موجود
          if (entity.getRole() == null) {
               entity.setRole(dto.getRole() != null ? dto.getRole() : Roles.EMPLOYER);
          }
     }
}
