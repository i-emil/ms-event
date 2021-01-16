package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.ParticipantEntity;

public interface ParticipantExtendedRepository {
    void addParticipant(Long participantTypeId, ParticipantEntity participant);
}

