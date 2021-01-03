package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.UserFoundEventEntity;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.ParticipantType;

public class ParticipantMapper {

    public static ParticipantDto foundEventToParticipant(UserFoundEventEntity userFoundEventEntity, ParticipantType participantType) {
        return ParticipantDto.builder()
                .eventId(userFoundEventEntity.getEvent().getId())
                .key(userFoundEventEntity.getKey())
                .type(participantType.toString()).build();
    }
}
