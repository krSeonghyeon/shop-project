package com.bcsd.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sorter {
    SALE_PRICE_ASC("salePriceAsc"),
    SALE_PRICE_DESC("salePriceDesc"),
    LATEST("latest");

    private final String value;

    public static Sorter fromValue(String value) {
        for (Sorter sorter : Sorter.values()) {
            if (sorter.value.equals(value)) {
                return sorter;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
