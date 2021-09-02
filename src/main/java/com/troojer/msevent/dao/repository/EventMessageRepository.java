package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.EventMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventMessageRepository extends JpaRepository<EventMessageEntity, Long> {

    Page<EventMessageEntity> getAllByEventId(Long eventId, Pageable pageable);
}
