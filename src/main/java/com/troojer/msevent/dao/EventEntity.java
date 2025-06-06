package com.troojer.msevent.dao;

import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
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

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
    @MapKey(name = "type")
    @Builder.Default
    private Map<ParticipantType, EventParticipantTypeEntity> participantsType = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
    private Set<EventTagEntity> tags;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
    private Set<EventLanguageEntity> languages;

    private String source;

    @Column(name = "is_join_in_app_enough")
    private Boolean isJoinInAppEnough;

    private int watched;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isFilterDisabled() {
        return minAge == null && participantsType.isEmpty();
    }

    public boolean isPrivate() {
        return password != null;
    }

    public boolean isLimitlessParticipating() {
        return participantsType == null || participantsType.isEmpty();
    }
}
