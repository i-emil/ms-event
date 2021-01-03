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
@ConsistentEventStart(message = "event.date.periodBeforeStart", param = "eventDate", groups = {CreateValidation.class, UpdateValidation.class})
@ConsistentEventDuration(message = "event.date.duration", param = "eventDate", groups = {CreateValidation.class, UpdateValidation.class})
public class EventDateDto {
    private String start;
    private String end;
}
