package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.ParticipantType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_participant_type")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "event")
public class EventParticipantTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "event_participant_type_seq")
    private Long id;

    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private ParticipantType type;

    private int total;

    private int accepted;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @EqualsAndHashCode.Include
    private EventEntity event;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isFree() {
        return accepted < total;
    }

    public void increaseAccepted() {
        if (accepted < total) accepted += 1;
    }

    public void decreaseAccepted() {
        if (accepted > 0) accepted -= 1;
    }

}
