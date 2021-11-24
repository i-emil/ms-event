package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import com.troojer.msevent.model.label.manageevent.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.HashMap;
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

    @Size(min = 5, max = 500, message = "event.description.pattern::5::500", groups = {CreateValidation.class, DescriptionUpdateValidation.class})
    private String description;

    @NotBlank(message = "event.cover.notBlank", groups = {CreateValidation.class, CoverUpdateValidation.class})
    private String cover;

    @ConsistentEventStart(message = "event.date.startDate", param = "date", groups = {CreateValidation.class, DatesUpdateValidation.class})
    private String startDate;

    @NotNull(groups = {CreateValidation.class, LocationUpdateValidation.class})
    private String locationId;

    @Size(min = 1, max = 5, message = "tag.tagList.size::{min}::{max}", groups = {CreateValidation.class, TagsUpdateValidation.class})
    @NotNull(groups = {CreateValidation.class, TagsUpdateValidation.class})
    @NotEmpty(groups = {CreateValidation.class, TagsUpdateValidation.class})
    private @Valid Set<TagDto> tags;

    //---NOT MANDATORY---

    @Range(min = 15, max = 60 * 24, message = "event.date.duration", groups = {CreateValidation.class, DurationUpdateValidation.class})
    private Integer duration;

    @Builder.Default
    private Map<ParticipantType, EventParticipantTypeDto> participantsType = new HashMap<>();

    @JsonInclude(NON_NULL)
    private @Valid BudgetDto budget;

    @JsonInclude(NON_NULL)
    @Builder.Default
    private @Valid InvitingDto inviting = new InvitingDto();

    // ---NOT NEEDED NOW ---

    @JsonInclude(NON_NULL)
    private @Valid AgeDto age;

    @JsonInclude(NON_NULL)
    @Size(max = 10, message = "language.languageList.size::{min}::{max}", groups = {CreateValidation.class, UpdateValidation.class})
    private Set<@Valid LanguageDto> languages;

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

    @JsonProperty(access = READ_ONLY)
    @Builder.Default
    private Boolean isJoinInAppEnough = true;

    @JsonProperty(access = READ_ONLY)
    private String source;
}
