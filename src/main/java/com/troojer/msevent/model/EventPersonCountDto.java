package com.troojer.msevent.model;

import com.troojer.msevent.constraints.ConsistentPersonCountParameters;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ConsistentPersonCountParameters(groups = {CreateValidation.class, UpdateValidation.class}, param = "personCount", message = "{event.personCount.zero}")
public class EventPersonCountDto {

    private int maleCount;

    private int femaleCount;

    private int allCount;
}
