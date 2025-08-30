package com.example.banksystem.Accountes;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountesTypesConverter implements AttributeConverter<AccountesTypes, String> {

    @Override
    public String convertToDatabaseColumn(AccountesTypes attribute) {
        if (attribute == null) return null;
        return attribute.getValue();  // هنا نحفظ النص الصغير (current، savings، ...)
    }

    @Override
    public AccountesTypes convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return AccountesTypes.fromValue(dbData);
    }
}
