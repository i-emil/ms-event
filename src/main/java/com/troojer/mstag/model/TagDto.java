package com.troojer.mstag.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Pattern(regexp = "[\\d\\w-]{3,20}", message = "{tag.value.pattern}")
    private String value;
}
