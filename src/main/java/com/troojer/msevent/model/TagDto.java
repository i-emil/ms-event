package com.troojer.msevent.model;

import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
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

    @NotBlank(message = "tag.value.notBlank", groups = {CreateValidation.class, UpdateValidation.class})
    @Pattern(regexp = "[\\d\\w-]{3,20}", message = "tag.value.pattern::3::20::a-z, 0-9, _, -", groups = {CreateValidation.class, UpdateValidation.class})
    private String value;
    private Long id;

    public void setValue(String value) {
        this.value = value.toLowerCase();
    }
}
