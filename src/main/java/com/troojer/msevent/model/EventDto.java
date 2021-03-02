package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import com.troojer.msevent.model.label.manageevent.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventDto {

    @NotBlank(message = "event.title.notBlank", groups = {CreateValidation.class, TitleUpdateValidation.class})
    @Pattern(regexp = "[a-zA-z0-9 -]{3,16}", message = "event.title.pattern::2::16::a-z, 0-9,  , -, _", groups = {CreateValidation.class, TitleUpdateValidation.class})
    private String title;

    @NotBlank(message = "event.description.notBlank", groups = {CreateValidation.class, DescriptionUpdateValidation.class})
    @Pattern(regexp = "[a-zA-z0-9 -]{10,150}", message = "event.description.pattern::1::150::a-z, 0-9,  , -, _", groups = {CreateValidation.class, DescriptionUpdateValidation.class})
    private String description;

    @NotBlank(message = "event.cover.notBlank", groups = {CreateValidation.class, CoverUpdateValidation.class})
    private String cover;

    @NotNull(message = "event.date.notNull", groups = {CreateValidation.class, DatesUpdateValidation.class})
    private @Valid StartEndDatesDto date;

    @JsonInclude(NON_NULL)
    private LocationDto location;

    @JsonInclude(NON_NULL)
    @NotNull
    @NotEmpty
    private Map<ParticipantType, EventParticipantTypeDto> participantsType;

    @JsonInclude(NON_NULL)
    @NotNull(message = "event.age.notNull", groups = CreateValidation.class)
    private @Valid AgeDto age;

    @JsonInclude(NON_NULL)
    @NotEmpty(message = "event.languages.notEmpty", groups = CreateValidation.class)
    @Size(min = 1, max = 10, message = "language.languageList.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private Set<@Valid LanguageDto> languages;

    //---NOT MANDATORY---
    @JsonInclude(NON_NULL)
    private @Valid BudgetDto budget;

    @JsonInclude(NON_NULL)
    @Size(max = 10, message = "tag.tagList.size::{min}::{max}", groups = {CreateValidation.class, TagsUpdateValidation.class})
    private @Valid Set<TagDto> tags;

    @JsonInclude(NON_NULL)
    private @Valid InvitingDto inviting;

//---READ ONLY FIELDS---

    @JsonInclude(NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ParticipantDto> participants;

    @JsonProperty(access = READ_ONLY)
    private String key;

    @JsonInclude(NON_NULL)
    @JsonProperty(access = READ_ONLY)
    private String authorId;

    @JsonProperty(access = READ_ONLY)
    private EventStatus status;

    @JsonInclude(value = NON_NULL)
    @JsonProperty(access = READ_ONLY)
    private String participationKey;

}
