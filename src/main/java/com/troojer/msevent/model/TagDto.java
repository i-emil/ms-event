package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagDto {
    @NotBlank(message = "{tag.value.notBlank}")
    @Pattern(regexp = "[\\d\\w-]{3,20}", message = "{tag.value.pattern}")
    private String value;
    private Long id;

    public void setValue(String value) {
        this.value = value.toLowerCase();
    }
}
