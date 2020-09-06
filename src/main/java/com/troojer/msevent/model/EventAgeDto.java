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
@ConsistentAgeParameters(param = "age", message = "{event.age.range}", groups = {CreateValidation.class, UpdateValidation.class})
public class EventAgeDto {
    private Integer min;
    private Integer max;
}
