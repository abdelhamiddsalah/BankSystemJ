package com.example.banksystem.Deposits;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepositMapper {

 @Mapping(source = "user.id", target = "userId")
    DepositDto depositdto(DepositEntity depositEntity);

 @Mapping(source = "userId", target = "user.id")
    DepositEntity depositentity(DepositDto depositdto);
}

