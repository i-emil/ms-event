package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@ConsistentAgeParameters(min = 18, max = 100, message = "event.age.incorrect::min::max", param = "eventAge", groups = {CreateValidation.class, UpdateValidation.class})
public class AgeDto {
    private Integer min;
    private Integer max;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer current;
}
