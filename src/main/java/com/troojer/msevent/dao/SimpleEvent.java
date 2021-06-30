package com.troojer.msevent.dao;

import com.troojer.msevent.model.EventParticipantTypeDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;

import java.time.ZonedDateTime;
import java.util.Map;

public interface SimpleEvent {

    Long getId();

    String getKey();

    String getTitle();

    String getDescription();

    Map<ParticipantType, EventParticipantTypeEntity> getParticipantsType();

    String getCover();

    ZonedDateTime getStartDate();

    Integer getDuration();

    EventStatus getStatus();

    Boolean getFilterDisabled();

    String getLocationId();

}
