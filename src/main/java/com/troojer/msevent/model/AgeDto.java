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

    public final static Integer MIN_AGE = 18;
    public final static Integer MAX_AGE = 100;

    @Builder.Default
    private Integer min = MIN_AGE;
    @Builder.Default
    private Integer max = MAX_AGE;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer current;
}
