package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import lombok.*;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "participant_seq")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "user_id")
    @EqualsAndHashCode.Include
    private String userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Enumerated(EnumType.STRING)
    private ParticipantType type;

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
