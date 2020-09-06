package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.EventPersonCountDto;

public class EventPersonCountMapper {
    public static EventPersonCountDto entityToDto(EventEntity entity) {
        return EventPersonCountDto.builder()
                .maleCount(entity.getMalePersonCount())
                .femaleCount(entity.getFemalePersonCount())
                .allCount(entity.getAllPersonCount())
                .build();
    }

    public static int dtoToMaleCount(EventPersonCountDto dto) {
        return dto.getMaleCount();
    }

    public static int dtoToFemaleCount(EventPersonCountDto dto) {
        return dto.getFemaleCount();
    }

    public static int dtoToAllCount(EventPersonCountDto dto) {
        return dto.getAllCount();
    }
}
