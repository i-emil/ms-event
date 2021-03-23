package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.EventMessageStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.troojer.msevent.model.enm.EventMessageStatus.OK;

@Entity
@Table(name = "event_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_message_seq_generator")
    @SequenceGenerator(name = "event_message_seq_generator", sequenceName = "event_message_seq")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private String userId;

    private String message;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EventMessageStatus status = OK;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
