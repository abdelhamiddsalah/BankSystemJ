package com.example.banksystem.Auth;

import com.example.banksystem.Employers.Auth.EmployerRepo;
import com.example.banksystem.Employers.Auth.EmplyerEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected EmployerRepo employerRepo;

    @Mapping(target = "employer", source = "employerId") // نحول من id إلى entity
    public abstract UserEntity toEntity(UserDto userDto);

    public abstract UserDto toDto(UserEntity userEntity);

    // تحويل Long → EmplyerEntity
    protected EmplyerEntity map(Long employerId) {
        if (employerId == null) {
            return null;
        }
        return employerRepo.findById(employerId).orElse(null);
    }

    @AfterMapping
    protected void afterMapping(UserDto dto, @MappingTarget UserEntity entity) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getPinCode() != null && !dto.getPinCode().isEmpty()) {
            entity.setPinCode(passwordEncoder.encode(dto.getPinCode()));
        }
    }
}
