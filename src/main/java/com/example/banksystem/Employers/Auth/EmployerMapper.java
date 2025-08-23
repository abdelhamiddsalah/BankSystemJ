package com.example.banksystem.Employers.Auth;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EmployerMapper {
     abstract EmplyerEntity emplyerEntity(EmployerDto employerDto);
     abstract EmployerDto employerDto(EmplyerEntity emplyerEntity) ;

}
