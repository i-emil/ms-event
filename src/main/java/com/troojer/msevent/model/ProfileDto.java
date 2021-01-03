package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileDto {

    private String userId;
    private String name;
    private String surname;
    private String avatar;
    private Double rating;

}
