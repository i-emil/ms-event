package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.constraints.ImageExist;
import com.troojer.msevent.constraints.IsLocationExist;
import com.troojer.msevent.constraints.NullOrNotBlank;
import com.troojer.msevent.constraints.PersonCountValidation;
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
public class EventDto {

    @NotBlank(message = "event.title.notBlank", groups = CreateValidation.class)
    @NullOrNotBlank(message = "event.title.nullOrNotBlank", param = "title", groups = UpdateValidation.class)
    @Pattern(regexp = "[a-zA-z0-9 -]{2,16}", message = "event.title.pattern::2::16::a-z, 0-9,  , -, _", groups = {CreateValidation.class, UpdateValidation.class})
    private String title;

    @NotBlank(message = "event.description.notBlank", groups = CreateValidation.class)
    @NullOrNotBlank(message = "event.description.nullOrNotBlank", param = "description", groups = {CreateValidation.class, UpdateValidation.class})
    @Pattern(regexp = "[a-zA-z0-9 -]{1,150}", message = "event.description.pattern::1::150::a-z, 0-9,  , -, _", groups = {CreateValidation.class, UpdateValidation.class})
    private String description;

    @NotBlank(message = "event.cover.notBlank", groups = {CreateValidation.class})
    @ImageExist(param = "cover", message = "event.cover.notExist", groups = {CreateValidation.class, UpdateValidation.class})
    private String cover;

    @NotNull(message = "event.date.notNull", groups = {CreateValidation.class})
    private @Valid EventDateDto date;

    @IsLocationExist(param = "locationId", message = "event.locationId.incorrect", groups = {CreateValidation.class, UpdateValidation.class})
    private LocationDto location;

    @PersonCountValidation(param = "total", message = "event.personCount.incorrect", groups = {CreateValidation.class, UpdateValidation.class})
    private Map<ParticipantType, EventParticipantTypeDto> participantsType;

    @NotNull(message = "event.age.notNull", groups = CreateValidation.class)
    private @Valid AgeDto age;

    @NotEmpty(message = "event.languages.notEmpty", groups = CreateValidation.class)
    @Size(min = 1, max = 10, message = "language.languageList.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private Set<@Valid LanguageDto> languages;

    //---NOT MANDATORY---
    private @Valid BudgetDto budget;

    @Size(max = 10, message = "tag.tagList.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private @Valid Set<TagDto> tags;

//---READ ONLY FIELDS---

    @JsonProperty(access = READ_ONLY)
    private String id;

    @JsonProperty(access = READ_ONLY)
    private String authorId;

    @JsonProperty(access = READ_ONLY)
    private EventStatus status;

    @JsonProperty(access = READ_ONLY)
    private List<ParticipantDto> participants;

    @JsonProperty(access = READ_ONLY)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String key;

}
