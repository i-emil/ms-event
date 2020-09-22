package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.EventLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagLanguageRepository extends JpaRepository<EventLanguageEntity, Long> {
    Set<EventLanguageEntity> getByEventId(Long eventId);

    Set<EventLanguageEntity> getByLanguageId(String languageId);
}
