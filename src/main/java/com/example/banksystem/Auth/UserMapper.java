/*package com.example.banksystem.Auth;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // 👈 علشان نتجنب التحذيرات
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // ✅ مش محتاجين نكتب @Mapping لكل حاجة
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "deposits", ignore = true)
    public abstract UserEntity toEntity(UserDto userDto);

    public abstract UserDto toDto(UserEntity userEntity);

    @AfterMapping
    protected void afterMappingToEntity(UserDto dto, @MappingTarget UserEntity entity) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getPinCode() != null && !dto.getPinCode().isEmpty()) {
            entity.setPinCode(passwordEncoder.encode(dto.getPinCode()));
        }
    }
}*/
