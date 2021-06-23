package com.troojer.msevent.model;

import com.troojer.msevent.constraints.FilterDateParameters;
import com.troojer.msevent.model.label.FilterValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FilterDateParameters(message = "event.date.startEndDate", param = "eventDate", groups = FilterValidation.class)
public class StartEndDatesDto {
    @Builder.Default
    private boolean disableDate = false;
    private String start;
    private String end;
}
