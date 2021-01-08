package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventParticipantTypeDto {

    private int total;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int accepted;

}
