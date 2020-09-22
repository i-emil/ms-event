package com.troojer.msevent.dao.repository;


import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Page<EventEntity> getAllByAuthorId(String userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE EventEntity ee SET ee.status=:status WHERE ee.endDate<=:date")
    void setStatusByEndDate(@Param("status") Status status, @Param("date") ZonedDateTime date);

}
