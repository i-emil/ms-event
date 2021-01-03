package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "event")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tags", "languages", "participantsType"})
@Where(clause = "status != 'DELETED'")
public class EventEntity {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(name = "author_id")
    private String authorId;

    private String title;

    private String description;

    private String cover;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "location_id")
    private String locationId;

    private Integer budget;

    private String currency;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.ACTIVE;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
    @MapKey(name = "type")
    private Map<ParticipantType, EventParticipantTypeEntity> participantsType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
    private Set<EventTagEntity> tags;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
    private Set<EventLanguageEntity> languages;

    private int watched;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
