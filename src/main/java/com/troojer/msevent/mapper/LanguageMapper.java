package com.troojer.msevent.mapper;


import com.troojer.msevent.client.LanguageClient;
import com.troojer.msevent.dao.EventLanguageEntity;
import com.troojer.msevent.model.LanguageDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LanguageMapper {

    private final Map<String, String> languagesMap;

    public LanguageMapper(LanguageClient languageClient) {
        this.languagesMap = languageClient.getLanguagesMap();
    }

    private LanguageDto entityToDto(EventLanguageEntity entity) {
        return LanguageDto.builder()
                .id(entity.getLanguageId())
                .nativeName(languagesMap.get(entity.getLanguageId()))
                .build();
    }

    private EventLanguageEntity dtoToEntity(LanguageDto languageDto) {
        return EventLanguageEntity.builder()
                .languageId(languageDto.getId().toLowerCase())
                .build();
    }

    public Set<LanguageDto> entitySetToDtoSet(Set<EventLanguageEntity> entitySet) {
        return (entitySet == null) ? null : entitySet.stream()
                .filter(entity -> languagesMap.containsKey(entity.getLanguageId()))
                .map(this::entityToDto).collect(Collectors.toSet());
    }

    public Set<EventLanguageEntity> dtoSetToEntitySet(Set<LanguageDto> dtoSet) {
        return (dtoSet == null) ? null : dtoSet.stream()
                .filter(dto -> languagesMap.containsKey(dto.getId().toLowerCase()))
                .map(this::dtoToEntity).collect(Collectors.toSet());
    }
}
