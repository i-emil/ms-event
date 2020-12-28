package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.constraints.ConsistentNumberRange;
import com.troojer.msevent.constraints.EventTypeValidation;
import com.troojer.msevent.constraints.NullOrNotBlank;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EventTypeValidation(message = "event.participantsType.wrong", groups = CreateValidation.class, param = "type")
public class EventDto {

    @JsonProperty(access = READ_ONLY)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String key;

    @JsonProperty(access = READ_ONLY)
    private String id;

    @JsonProperty(access = READ_ONLY)
    private String authorId;

    @NotBlank(message = "event.title.notBlank", groups = CreateValidation.class)
    @NullOrNotBlank(message = "event.title.nullOrNotBlank", param = "title", groups = UpdateValidation.class)
    private String title;

    @NotBlank(message = "event.description.notBlank", groups = CreateValidation.class)
    @NullOrNotBlank(message = "event.description.nullOrNotBlank", param = "description", groups = {CreateValidation.class, UpdateValidation.class})
    @Pattern(regexp = "[a-zA-z0-9 -]{1,150}", message = "event.description.pattern::1::150::a-z, 0-9,  , -, _", groups = {CreateValidation.class, UpdateValidation.class})
    private String description;

    @NotNull(message = "event.date.notNull", groups = {CreateValidation.class})
    private @Valid EventDateDto date;

    @NotNull(message = "location.id.notNull", groups = CreateValidation.class)
    @Positive(message = "location.id.positive", groups = {CreateValidation.class, UpdateValidation.class})
    private Long locationId;

    @NotNull(message = "event.age.notNull", groups = CreateValidation.class)
    private @Valid AgeDto age;

    @NotEmpty(message = "event.languages.notEmpty", groups = CreateValidation.class)
    @Size(min = 1, max = 10, message = "language.languageList.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private Set<@Valid LanguageDto> languages;

    private Map<ParticipantType, EventParticipantTypeDto> participantsType;

    @ConsistentNumberRange(param = "budget", min = 0, max = 1000, message = "event.budget.range::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private Integer budget;

    @Size(max = 10, message = "tag.tagList.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private @Valid Set<TagDto> tags;

    @JsonProperty(access = READ_ONLY)
    private List<ParticipantDto> participants;

    @JsonProperty(access = READ_ONLY)
    private EventStatus status;
}
