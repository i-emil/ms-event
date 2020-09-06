package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.troojer.msevent.constraints.ConsistentDateParameters;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ConsistentDateParameters(param = "date", message = "{event.date.startEndDate}", groups = {CreateValidation.class, UpdateValidation.class})
public class EventDateDto {
    private String start;
    private String end;
}
