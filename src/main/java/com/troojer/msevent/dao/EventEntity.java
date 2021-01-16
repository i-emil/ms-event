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
import java.util.*;

@Entity
@Table(name = "event")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq_generator")
    @SequenceGenerator(name = "event_seq_generator", sequenceName = "event_seq")
    @EqualsAndHashCode.Include
    private Long id;

    @Builder.Default
    @EqualsAndHashCode.Include
    private String key = UUID.randomUUID().toString();

    @Column(name = "author_id")
    @EqualsAndHashCode.Include
    private String authorId;

    @EqualsAndHashCode.Include
    private String title;

    @EqualsAndHashCode.Include
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

//    @Builder.Default
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
//    private final List<ParticipantEntity> participants = new ArrayList<>();

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
