package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.model.enm.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long>, ParticipantExtendedRepository {

    Optional<ParticipantEntity> getFirstByEventIdAndUserIdAndStatusIn(Long eventId, String userId, List<ParticipantStatus> statuses);

    @Query("SELECT p FROM ParticipantEntity p " +
            "INNER JOIN EventEntity e ON p.event = e " +
            "WHERE e.key = :eventKey " +
            "AND p.status in :statuses")
    List<ParticipantEntity> getParticipants(String eventKey, List<ParticipantStatus> statuses);
}

