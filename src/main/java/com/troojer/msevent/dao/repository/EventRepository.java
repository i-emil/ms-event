package com.troojer.msevent.dao.repository;


import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> getFirstByKey(String key);

    Optional<EventEntity> getFirstByInviteKeyAndInviteActive(String inviteKey, boolean isInviteActive);

    @Query(value = "SELECT e FROM EventEntity e " +
            "WHERE e.startDate >= :from AND e.startDate <= :until " +
            "AND e.authorId = :userId ")
    Page<SimpleEvent> getAuthorEventsByDate(ZonedDateTime from, ZonedDateTime until, String userId, Pageable pageable);

    Page<SimpleEvent> getAllByAuthorId(String userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE EventEntity ee SET ee.status=:status WHERE ee.startDate>:from AND ee.startDate<=:to")
    void setStatusByEndDate(EventStatus status, ZonedDateTime from, ZonedDateTime to);

    @Query(value = "SELECT e FROM EventEntity e " +
            "JOIN ParticipantEntity p ON p.event = e " +
            "WHERE e.startDate >= :after AND e.startDate <= :before " +
            "AND p.userId = :userId " +
            "AND e.status IN :eventStatuses " +
            "AND p.status in :participantStatuses")
    Page<SimpleEvent> getEventsPageByParticipantAndDate(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses, Pageable pageable);

    @Query(value = "SELECT e FROM EventEntity e " +
            "JOIN ParticipantEntity p ON p.event = e " +
            "AND p.userId = :userId " +
            "AND e.status IN :eventStatuses " +
            "AND p.status in :participantStatuses")
    Page<SimpleEvent> getEventsPageByParticipant(String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses, Pageable pageable);

    @Query(value = "SELECT e FROM EventEntity e " +
            "WHERE e.startDate >= :after AND e.startDate <= :before " +
            "AND e.status IN :eventStatuses ")
    List<EventEntity> getEventList(ZonedDateTime after, ZonedDateTime before, List<EventStatus> eventStatuses);

    @Query(value = "SELECT e FROM EventEntity e " +
            "JOIN ParticipantEntity p ON p.event = e " +
            "WHERE e.startDate >= :after AND e.startDate <= :before " +
            "AND p.userId = :userId " +
            "AND e.status IN :eventStatuses " +
            "AND p.status in :participantStatuses")
    List<EventEntity> getParticipantEvents(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses);

}