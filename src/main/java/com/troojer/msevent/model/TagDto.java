package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.manageevent.TagsUpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagDto {

    @NotBlank(message = "tag.value.notBlank", groups = {CreateValidation.class, TagsUpdateValidation.class})
    @Pattern(regexp = "[\\d\\w-]{3,20}", message = "tag.value.pattern::3::20::a-z, 0-9, _, -", groups = {CreateValidation.class, TagsUpdateValidation.class})
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String value;
    private Long id;

    public void setValue(String value) {
        this.value = value.toLowerCase();
    }
}
