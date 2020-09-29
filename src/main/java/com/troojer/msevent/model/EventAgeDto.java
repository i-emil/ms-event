package com.troojer.msevent.model;

import com.troojer.msevent.constraints.ConsistentAgeParameters;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ConsistentAgeParameters(min = 18, max = 110, message = "event.age.range::min::max", param = "eventAge", groups = {CreateValidation.class, UpdateValidation.class})
public class EventAgeDto {
    private Integer min;
    private Integer max;
}
