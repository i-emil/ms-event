package com.troojer.msevent.mapper;

import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.dao.RandomEventEntity;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.ParticipantType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticipantMapper {

    private final ProfileClient profileClient;

    public ParticipantMapper(ProfileClient profileClient) {
        this.profileClient = profileClient;
    }

    //todo ubrat
    public static ParticipantDto foundEventToParticipant(RandomEventEntity randomEventEntity, ParticipantType participantType) {
        return ParticipantDto.builder()
                .type(participantType.toString()).build();
    }

    public List<ParticipantDto> getParticipantsFromEvent(EventEntity eventEntity) {
        return eventEntity.getParticipantsType().values().stream()
                .map(EventParticipantTypeEntity::getParticipants)
                .flatMap(List::stream).map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public ParticipantDto entityToDto(ParticipantEntity entity) {
        return ParticipantDto.builder()
                .profile(profileClient.getProfile(entity.getUserId()))
                .type(String.valueOf(entity.getEventParticipantType().getType()))
                .build();
    }

}
