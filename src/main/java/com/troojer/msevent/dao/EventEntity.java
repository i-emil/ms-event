package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private Integer duration;

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

    @Column(name = "invite_active")
    private boolean inviteActive;

    @Column(name = "invite_key")
    @Builder.Default
    private String inviteKey = UUID.randomUUID().toString().replace("-","");

    @Column(name = "invite_password")
    private String invitePassword;

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
