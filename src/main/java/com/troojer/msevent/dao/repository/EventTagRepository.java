package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.EventTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EventTagRepository extends JpaRepository<EventTagEntity, Long> {
    Set<EventTagEntity> getByEventId(Long eventId);

    Set<EventTagEntity> getByTagId(String tagId);
}
