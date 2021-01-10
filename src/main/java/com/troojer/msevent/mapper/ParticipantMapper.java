package com.troojer.msevent.mapper;

import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.model.ParticipantDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticipantMapper {

    private final ProfileClient profileClient;

    public ParticipantMapper(ProfileClient profileClient) {
        this.profileClient = profileClient;
    }

    public ParticipantDto entityToDto(ParticipantEntity entity) {
        return ParticipantDto.builder()
                .profile(profileClient.getProfile(entity.getUserId()))
                .type(entity.getType().name())
                .build();
    }

    public List<ParticipantDto> entitiesToDtos(List<ParticipantEntity> entities) {
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }


}
