package com.troojer.msevent.dao.repository;


import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.EventType;
import com.troojer.msevent.model.enm.ParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, String> {

    Page<EventEntity> getAllByAuthorId(String userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE EventEntity ee SET ee.status=:status WHERE ee.endDate<=:date")
    void setStatusByEndDate(@Param("status") EventStatus status, @Param("date") ZonedDateTime date);

    @Query(value = "SELECT e FROM EventEntity e, EventLanguageEntity el, EventParticipantTypeEntity ept " +
            "WHERE e.startDate >= :afterDate AND e.startDate <= :beforeDate " +
            "AND e.status='ACTIVE' " +
            "AND e.type IN :types " +
            "AND e.authorId <> :userId " +
            "AND e.id NOT IN :eventIdList " +
            "AND e.minAge BETWEEN :minAge AND :maxAge " +
            "AND e.maxAge BETWEEN  :minAge AND :maxAge " +
            "AND e = ept.event " +
            "AND ((ept.type=:participantType AND ept.total-ept.accepted>0) OR (ept.type='ALL' AND ept.total-ept.accepted>0)) " +
            "AND e.budget<=:budget " +
            "AND e = el.event " +
            "AND el.languageId IN :languagesId " +
            "ORDER BY e.id ")
    List<EventEntity> getFirstByFilter(ZonedDateTime afterDate, ZonedDateTime beforeDate, List<EventType> types, Integer minAge, Integer maxAge, ParticipantType participantType, Integer budget, String userId, List<String> eventIdList, List<String> languagesId, Pageable pageable);

}
