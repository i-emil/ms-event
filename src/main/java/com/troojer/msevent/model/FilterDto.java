package com.troojer.msevent.model;

import com.troojer.msevent.model.enm.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {

    @NotNull
    //todo validation
    private StartEndDatesDto dates;

    private AgeDto age;

    private Gender gender;
    
    private Set<@Valid LanguageDto> languages;

    private Long tagId;

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.gender = profileInfo.getGender();
        age = new AgeDto();
        this.age.setCurrent(profileInfo.getCurrentAge());
    }
}
