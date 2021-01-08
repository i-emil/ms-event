package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantEntityRepository extends JpaRepository<ParticipantEntity, Long> {
}
