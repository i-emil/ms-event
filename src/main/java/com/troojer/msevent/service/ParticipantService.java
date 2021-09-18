package com.troojer.msevent.service;

import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantStatus;

import java.util.List;

public interface ParticipantService {

    boolean checkParticipating(String key);

    List<ParticipantDto> getOkParticipants(String eventKey);

    List<ParticipantDto> getParticipants(String eventKey, List<ParticipantStatus> statuses);

    void joinEvent(String eventKey, String userId, Gender gender);

    void deleteFromEvent(String eventKey);

    boolean deleteFromEvent(String eventKey, String userId, ParticipantStatus reason);

    void leftInappropriateEvents(Integer age, Gender gender);
}
