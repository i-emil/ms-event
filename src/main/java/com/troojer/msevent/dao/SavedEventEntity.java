package com.troojer.msevent.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "saved_event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SavedEventEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "saved_event_seq")
    @SequenceGenerator(name = "saved_event_seq", sequenceName = "saved_event_seq")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Builder.Default
    private Boolean deleted = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
