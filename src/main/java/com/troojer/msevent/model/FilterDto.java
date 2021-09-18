package com.troojer.msevent.model;

import com.troojer.msevent.constraints.FilterDateParameters;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.label.FilterValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {

    @NotNull
    @FilterDateParameters(groups = FilterValidation.class, param = "dates", message = "startDate must be less than endDate")
    private StartEndDatesDto dates;

    @Positive
    private Long tagId;

}
