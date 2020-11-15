package com.troojer.msevent.mapper;


import com.troojer.msevent.client.LanguageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventLanguageEntity;
import com.troojer.msevent.model.LanguageDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LanguageMapper {

    private final Map<String, String> languagesMap;

    public LanguageMapper(LanguageClient languageClient) {
        this.languagesMap = languageClient.getLanguagesMap();
    }

    public static List<String> dtosToIds(Collection<LanguageDto> languageDtos) {
        return languageDtos.stream().map(LanguageDto::getId).collect(Collectors.toList());
    }

    private LanguageDto entityToDto(EventLanguageEntity entity) {
        return LanguageDto.builder()
                .id(entity.getLanguageId())
                .nativeName(languagesMap.get(entity.getLanguageId()))
                .build();
    }

    private EventLanguageEntity dtoToEntity(LanguageDto languageDto, EventEntity event) {
        return EventLanguageEntity.builder()
                .languageId(languageDto.getId().toLowerCase())
                .event(event)
                .build();
    }

    public Set<LanguageDto> entitySetToDtoSet(Collection<EventLanguageEntity> entitySet) {
        return (entitySet == null) ? Set.of() : entitySet.stream()
                .filter(entity -> languagesMap.containsKey(entity.getLanguageId()))
                .map(this::entityToDto).collect(Collectors.toSet());
    }

    public Set<EventLanguageEntity> dtoSetToEntitySet(Collection<LanguageDto> dtoSet, EventEntity event) {
        return (dtoSet == null) ? Set.of() : dtoSet.stream()
                .filter(dto -> languagesMap.containsKey(dto.getId().toLowerCase()))
                .map(language -> dtoToEntity(language, event)).collect(Collectors.toSet());
    }
}
