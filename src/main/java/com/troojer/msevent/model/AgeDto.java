package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AgeDto {
    private Integer min;
    private Integer max;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer current;
}
