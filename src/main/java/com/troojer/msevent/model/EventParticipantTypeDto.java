package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventParticipantTypeDto {

    @Positive
    private int total;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int accepted;

}
