package com.troojer.msevent.service;

import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;

import java.util.List;

public interface ParticipantService {

    List<ParticipantDto> getParticipants(String eventKey, List<ParticipantStatus> statuses);

    boolean addParticipant(String eventKey, String userId, ParticipantType userType);

    boolean deleteParticipant(String eventKey, String userId, ParticipantStatus reason);
}
