package com.example.banksystem.Accountes;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    AccountDto toDto(AccountEntity entity);


    AccountEntity toEntity(AccountDto dto);
}

