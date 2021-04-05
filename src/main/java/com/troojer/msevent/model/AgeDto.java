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
    @Builder.Default
    private Integer min = 18;
    @Builder.Default
    private Integer max = 100;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer current;
}
