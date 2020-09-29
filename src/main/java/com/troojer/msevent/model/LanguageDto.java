package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LanguageDto {
    @NotBlank(message = "language.id.notBlank", groups = {CreateValidation.class, UpdateValidation.class})
    @Pattern(regexp = "[a-z]{2}", message = "language.id.pattern::aa", groups = {CreateValidation.class, UpdateValidation.class})
    private String id;
    @JsonProperty(access = Access.READ_ONLY)
    private String nativeName;
}
