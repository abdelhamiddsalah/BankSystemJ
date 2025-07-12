package com.example.banksystem.Accountes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountesTypes {
    CURRENT("current"),
    SAVINGS("savings"),
    FOREIGN_CURRENCY("foreign_currency"),
    CREDIT_CARD("credit_card");

    private final String value;

    AccountesTypes(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AccountesTypes fromValue(String value) {
        for (AccountesTypes type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + value);
    }
}
