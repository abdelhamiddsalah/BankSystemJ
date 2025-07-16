package com.example.banksystem.Accountes;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {


// @Mapping(source = "accountType", target = "accountType")
    AccountDto toDto(AccountEntity entity);


 //@Mapping(source = "accountType", target = "accountType")
    AccountEntity toEntity(AccountDto dto);
}

