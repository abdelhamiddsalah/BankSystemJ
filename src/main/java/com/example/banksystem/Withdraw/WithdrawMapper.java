package com.example.banksystem.Withdraw;

import com.example.banksystem.Withdraw.WithdrawDto;
import com.example.banksystem.Withdraw.WithdrawEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawMapper {


    WithdrawEntity toEntity(WithdrawDto withdrawDto);

    WithdrawDto toDto(WithdrawEntity withdrawEntity);
}
