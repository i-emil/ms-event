package com.troojer.msevent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

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
