package com.troojer.msevent.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "code")
public class CurrencyDto {

    private String code;

    private String name;

    private String symbol;

    public CurrencyDto(String code) {
        this.code = code;
    }
}
