package com.troojer.msevent.mapper;


import com.troojer.msevent.dao.EventLanguageEntity;
import com.troojer.msevent.model.LanguageDto;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageMapper {
    private static LanguageDto entityToDto(EventLanguageEntity entity, Map<String, String> languagesMap) {
        return LanguageDto.builder()
                .id(entity.getLanguageId())
                .nativeName(languagesMap.get(entity.getLanguageId()))
                .build();
    }

    private static EventLanguageEntity dtoToEntity(Long eventId, LanguageDto languageDto) {
        return EventLanguageEntity.builder()
                .languageId(languageDto.getId())
                .eventId(eventId)
                .build();
    }

    public static Set<LanguageDto> entitySetToDtoSet(Set<EventLanguageEntity> entitySet, Map<String, String> allLanguagesMap) {
        return (entitySet == null) ? null : entitySet.stream()
                .filter(entity->allLanguagesMap.containsKey(entity.getLanguageId()))
                .map(entity -> LanguageMapper.entityToDto(entity, allLanguagesMap)).collect(Collectors.toSet());
    }

    public static Set<EventLanguageEntity> dtoSetToEntitySet(Long eventId, Set<LanguageDto> dtoSet, Map<String, String> allLanguagesMap) {
        return (dtoSet == null) ? null : dtoSet.stream()
                .filter(dto->allLanguagesMap.containsKey(dto.getId()))
                .map(dto -> LanguageMapper.dtoToEntity(eventId, dto)).collect(Collectors.toSet());
    }
}
