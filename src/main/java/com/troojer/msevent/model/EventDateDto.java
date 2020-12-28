package com.troojer.msevent.model;

import com.troojer.msevent.constraints.ConsistentEventDuration;
import com.troojer.msevent.constraints.ConsistentEventStart;
import com.troojer.msevent.constraints.EventDateParameters;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EventDateParameters(message = "event.date.startEndDate", param = "eventDate", groups = {CreateValidation.class, UpdateValidation.class})
@ConsistentEventStart(period=7, message = "event.date.periodBeforeStart::{period}", param = "eventDate", groups = {CreateValidation.class, UpdateValidation.class})
@ConsistentEventDuration(duration = 24, message = "event.date.duration::{duration}", param = "eventDate", groups = {CreateValidation.class, UpdateValidation.class})
public class EventDateDto {
    private String start;
    private String end;
}
