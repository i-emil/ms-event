package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {

    private AgeDto age;

    private Long locationId;

    private Integer budget;

    private Set<LanguageDto> languages;

    private String gender;

    private String coupleId;

}
