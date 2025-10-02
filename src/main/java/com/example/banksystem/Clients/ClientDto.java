package com.example.banksystem.Clients;

import com.example.banksystem.Auth.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private Long id;

    @NotNull(message = "User must be provided")
    private UserEntity user;
}
