package com.troojer.msevent.model;

import java.util.*;
import com.troojer.msevent.constraints.FilterDateParameters;
import com.troojer.msevent.model.label.FilterValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {

    @NotNull
    @FilterDateParameters(groups = FilterValidation.class, param = "dates", message = "startDate must be less than endDate")
    private StartEndDatesDto dates;

    @Size(min = 1, max=5)
    private Set<Long> tagIdList;

}
