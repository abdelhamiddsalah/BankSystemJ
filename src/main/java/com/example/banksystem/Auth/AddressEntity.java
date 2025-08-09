package com.example.banksystem.Auth;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class AddressEntity {

    private String city;
    private String ZipCode;
}
