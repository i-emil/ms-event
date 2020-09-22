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
    private String authorId;

    @NotBlank(message = "{event.title.notBlank}", groups = CreateValidation.class)
    @NullOrNotBlank(message = "{event.title.notBlank}", param = "eventTitle", groups = UpdateValidation.class)
    private String title;

    @NotBlank(message = "{event.description.notBlank}", groups = CreateValidation.class)
    @Pattern(regexp = ".[\\S\\s]{0,149}", message = "{event.description.pattern}", groups = {CreateValidation.class, UpdateValidation.class})
    private String description;

    @NotNull(message = "{event.date.notNull}", groups = {CreateValidation.class})
    private @Valid EventDateDto date;

    @NotNull(message = "{event.location.notNull}", groups = CreateValidation.class)
    @Positive(message = "{event.location.positive}", groups = {CreateValidation.class, UpdateValidation.class})
    private Long locationId;

    @NotNull(message = "{event.age.notNull}", groups = CreateValidation.class)
    private @Valid EventAgeDto age;

    @NullOrNotEmpty(message = "{event.languages.notEmpty}", param = "eventLanguages", groups = UpdateValidation.class)
    @NotEmpty(message = "{event.languages.notEmpty}", groups = CreateValidation.class)
    private Set<LanguageDto> languages;

    @NotNull(message = "{event.personCount.notNull}", groups = CreateValidation.class)
    private @Valid EventPersonCountDto personCount;

    @NotNull(message = "{event.category.notNull}", groups = CreateValidation.class)
    private CategoryDto category;

    @PositiveOrZero(message = "{event.budget.positive}", groups = {CreateValidation.class, UpdateValidation.class})
    private Integer budget;

    @Size(max = 10, message = "{tag.list.size}", groups = {CreateValidation.class, UpdateValidation.class})
    private @Valid Set<TagDto> tags;

    @JsonProperty(access = READ_ONLY)
    private Status status;

    @JsonProperty(access = READ_ONLY)
    private int watched;

}
