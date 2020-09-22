package com.troojer.msevent.dao;

import com.troojer.msevent.model.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "event")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tags", "languages"})
@Where(clause = "status != 'DELETED'")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "event_seq")
    private Long id;

    @Column(name = "author_id")
    private String authorId;

    private String title;

    private String description;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "location_id")
    private Long locationId;

    private Integer budget;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "male_person_count")
    private Integer malePersonCount;

    @Column(name = "female_person_count")
    private Integer femalePersonCount;

    @Column(name = "all_person_count")
    private Integer allPersonCount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Set<EventTagEntity> tags;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Set<EventLanguageEntity> languages;

    private int watched;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
