package com.troojer.msevent.mapper;


import com.troojer.msevent.client.TagClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventTagEntity;
import com.troojer.msevent.model.TagDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    private final TagClient tagClient;

    public TagMapper(TagClient tagClient) {
        this.tagClient = tagClient;
    }

    public Set<TagDto> entitySetToDtoSet(Set<EventTagEntity> entitySet) {
        return (entitySet == null || entitySet.isEmpty()) ? Set.of() : entitySet.stream()
                .map(x -> TagDto.builder().id(x.getTagId()).build()).collect(Collectors.toSet());
    }

    public Set<EventTagEntity> dtoSetToEntitySet(Set<TagDto> dtoSet, EventEntity event) {
        return (dtoSet == null || dtoSet.isEmpty()) ? Set.of() : dtoSet.stream().map(x -> dtoToEntity(x, event)).collect(Collectors.toSet());
    }

    private EventTagEntity dtoToEntity(TagDto dto, EventEntity event) {
        return EventTagEntity.builder().tagId(dto.getId()).event(event).build();
    }
}
