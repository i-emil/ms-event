package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.manageevent.LocationUpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull(groups = {CreateValidation.class, LocationUpdateValidation.class})
    private String id;

    private String name;

    private String address;

    private String placeId;

    @JsonInclude(NON_NULL)
    private String region;

    @JsonInclude(NON_NULL)
    private Double lat;

    @JsonInclude(NON_NULL)
    private Double lng;

}
