package com.troojer.msevent.model;

import com.troojer.msevent.model.enm.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileInfo {
    private Integer currentAge;
    private Gender gender;
}
