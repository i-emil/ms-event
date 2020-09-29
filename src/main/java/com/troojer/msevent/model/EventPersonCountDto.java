package com.troojer.msevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventPersonCountDto {

    private int maleCount;

    private int femaleCount;

    private int allCount;
}
