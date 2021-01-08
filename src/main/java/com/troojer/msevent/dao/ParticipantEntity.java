package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.ParticipantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participant")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "participant_seq")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "event_participant_type_id")
    private EventParticipantTypeEntity eventParticipantType;

    private String key;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ParticipantStatus status = ParticipantStatus.OK;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
