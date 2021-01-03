package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserPlanDto {

    private String userPlan;

    private @Valid UserPlanDateDto date;

    static public class UserPlanDateDto {
        private String start;
        private String end;
    }
}