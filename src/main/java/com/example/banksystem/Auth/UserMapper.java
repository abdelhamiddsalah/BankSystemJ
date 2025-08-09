package com.example.banksystem.Auth;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;


    public abstract UserEntity toEntity(UserDto userDto);

    public abstract UserDto toDto(UserEntity userEntity);

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
