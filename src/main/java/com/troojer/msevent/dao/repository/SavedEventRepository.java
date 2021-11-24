package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.SavedEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SavedEventRepository extends JpaRepository<SavedEventEntity, Long> {

    Page<SavedEventEntity> getAllByUserIdAndDeleted(String userId, Boolean isDeleted, Pageable pageable);

    @Query(value = "SELECT se FROM SavedEventEntity se WHERE se.userId = :userId AND se.event.key=:eventKey")
    Optional<SavedEventEntity> getByUserAndEventKey(String userId, String eventKey);

}
