package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.UserFoundEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "random_event")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RandomEventEntity {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserFoundEventStatus status = UserFoundEventStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static RandomEventEntity create(String userId, EventEntity eventEntity) {
        return RandomEventEntity.builder().userId(userId).event(eventEntity)
                .build();
    }
}
