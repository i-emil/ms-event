package com.troojer.msevent.dao.repository;


import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.enm.EventStatus;
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
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> getFirstByKey(String key);

    Page<EventEntity> getAllByAuthorId(String userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE EventEntity ee SET ee.status=:status WHERE ee.endDate<=:date")
    void setStatusByEndDate(@Param("status") EventStatus status, @Param("date") ZonedDateTime date);

    //todo couple
    @Query(value = "SELECT e FROM EventEntity e, EventLanguageEntity el, EventParticipantTypeEntity ept " +
            "WHERE (-1L IN :eventsIdForCheck OR e.id IN :eventsIdForCheck) " +
            "AND e.startDate >= :afterDate AND e.startDate <= :beforeDate " +
            "AND e.status='ACTIVE' " +
            "AND e.minAge BETWEEN :minAge AND :maxAge " +
            "AND e.maxAge BETWEEN  :minAge AND :maxAge " +
            "AND :currentAge BETWEEN  e.minAge AND e.maxAge " +
            "AND e = ept.event " +
            "AND ((ept.type=:participantType AND ept.total-ept.accepted>0) OR (ept.type='ALL' AND ept.total-ept.accepted>0)) " +
            "AND e = el.event " +
            "AND el.languageId IN :languagesId " +
            "AND e.id NOT IN :eventsExceptList " +
            "AND e.authorId <> :userId ")
    List<EventEntity> getListByFilter(List<Long> eventsIdForCheck, ZonedDateTime afterDate, ZonedDateTime beforeDate, Integer minAge, Integer maxAge, Integer currentAge, ParticipantType participantType, List<String> languagesId, List<Long> eventsExceptList, String userId, Pageable pageable);

    @Query(value = "SELECT e FROM EventEntity e " +
            "JOIN ParticipantEntity p ON p.event = e " +
            "WHERE e.status in :statuses " +
            "AND p.userId = :userId")
    List<EventEntity> getEventsByParticipant(String userId, List<EventStatus> statuses);

}