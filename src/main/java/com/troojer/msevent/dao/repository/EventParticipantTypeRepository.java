package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.EventParticipantTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantTypeRepository extends JpaRepository<EventParticipantTypeEntity, Long> {
}
