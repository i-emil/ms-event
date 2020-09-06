package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.constraints.NullOrNotBlank;
import com.troojer.msevent.constraints.NullOrNotEmpty;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventDto {

    @JsonProperty(access = READ_ONLY)
    private Long id;

    @JsonProperty(access = READ_ONLY)
    private String userId;

    @NotBlank(message = "{event.title.notBlank}", groups = CreateValidation.class)
    @NullOrNotBlank(message = "{event.title.notBlank}", groups = UpdateValidation.class)
    private String title;

    @Pattern(regexp = ".[\\S\\s]{0,149}", message = "{event.description.pattern}", groups = {CreateValidation.class, UpdateValidation.class})
    private String description;

    @NotNull(message = "{event.date.notNull}", groups = {CreateValidation.class})
    private @Valid EventDateDto date;

    @NotNull(message = "{event.location.notNull}", groups = CreateValidation.class)
    @Positive(message = "{event.location.positive}", groups = {CreateValidation.class, UpdateValidation.class})
    private Long locationId;

    @Positive(message = "{event.budget.positive}", groups = {CreateValidation.class, UpdateValidation.class})
    private Integer budget;

    @NotNull(message = "{event.age.notNull}", groups = CreateValidation.class)
    private @Valid EventAgeDto age;

    @JsonProperty(access = READ_ONLY)
    private Status status;

    @NullOrNotEmpty(message = "{event.languages.notEmpty}", groups = UpdateValidation.class)
    @NotEmpty(message = "{event.languages.notEmpty}", groups = CreateValidation.class)
    private Set<LanguageDto> languages;

    @NotNull(message = "{event.personCount.notNull}", groups = CreateValidation.class)
    private @Valid EventPersonCountDto personCount;

//Todo  @NotNull()
    private CategoryDto category;
}
