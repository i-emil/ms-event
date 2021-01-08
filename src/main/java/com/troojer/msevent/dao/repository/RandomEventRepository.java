package com.troojer.msevent.dao.repository;


import com.troojer.msevent.dao.RandomEventEntity;
import com.troojer.msevent.model.enm.UserFoundEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RandomEventRepository extends JpaRepository<RandomEventEntity, String> {

    Optional<RandomEventEntity> getFirstByIdAndUserIdAndStatusIn( String id, String userId, List<UserFoundEventStatus> status);

    Optional<RandomEventEntity> getFirstByUserIdAndStatusIn(String userId, List<UserFoundEventStatus> status);

    List<RandomEventEntity> getAllByUserIdAndStatusIn(String userId, List<UserFoundEventStatus> status);

    List<RandomEventEntity> getAllByUpdatedAtBefore(LocalDateTime dateTime);
}
