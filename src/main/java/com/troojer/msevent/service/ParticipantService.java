package com.troojer.msevent.service;

import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;

import java.util.List;

public interface ParticipantService {

    List<ParticipantDto> getParticipants(String eventKey, List<ParticipantStatus> statuses);

    void joinEvent(String eventKey, String userId);

    void deleteFromEvent(String eventKey);

    boolean deleteFromEvent(String eventKey, String userId, ParticipantStatus reason);

    void leftInappropriateEvents();
}
