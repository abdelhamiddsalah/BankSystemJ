package com.example.banksystem.Employers.Auth;

import com.example.banksystem.Auth.UserDto;
import com.example.banksystem.Auth.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class EmployerMapper {
     @Autowired
     protected PasswordEncoder passwordEncoder;

     abstract EmplyerEntity emplyerEntity(EmployerDto employerDto);
     abstract EmployerDto employerDto(EmplyerEntity emplyerEntity) ;

     @AfterMapping
     protected void afterMapping(EmployerDto dto, @MappingTarget EmplyerEntity entity) {
          if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
               entity.setPassword(passwordEncoder.encode(dto.getPassword()));
          }
          if (dto.getPincode() != null && !dto.getPincode().isEmpty()) {
               entity.setPincode(passwordEncoder.encode(dto.getPincode()));
          }
     }
}

