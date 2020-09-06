package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LanguageDto {
    private String id;
    @JsonProperty(access = Access.READ_ONLY)
    private String nativeName;
}
