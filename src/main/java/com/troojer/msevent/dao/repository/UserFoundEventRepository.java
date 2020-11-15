package com.troojer.msevent.dao.repository;


import com.troojer.msevent.dao.UserFoundEventEntity;
import com.troojer.msevent.model.enm.UserFoundEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserFoundEventRepository extends JpaRepository<UserFoundEventEntity, Long> {

    List<UserFoundEventEntity> getFirstByUserIdAndStatusInAndKey(String userId, List<UserFoundEventStatus> status, String key);

    List<UserFoundEventEntity> getAllByUserIdAndStatusIn(String userId, List<UserFoundEventStatus> status);

    List<UserFoundEventEntity> getAllByUpdatedAtBefore(LocalDateTime dateTime);
}
