package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ParticipantDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String type;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProfileDto profile;
}
