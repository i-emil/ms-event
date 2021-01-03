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
public class ParticipantDto {

    private ProfileDto profile;

    @NotBlank(message = "participant.key.notBlank")
    private String key;

    @NotBlank(message = "participant.eventId.notBlank")
    private String eventId;

    @NotBlank(message = "participant.gender.notBlank")
    @Pattern(regexp = "MALE|FEMALE|ALL", message = "participant.gender.pattern::{regexp}")
    private String type;

}
