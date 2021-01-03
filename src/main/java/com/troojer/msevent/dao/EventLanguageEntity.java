package com.troojer.msevent.dao;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_language")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "event")
public class EventLanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "event_language_seq")
    private Long id;

    @JoinColumn(name = "event_id")
    @EqualsAndHashCode.Include
    @ManyToOne
    private EventEntity event;

    @Column(name = "language_id")
    @EqualsAndHashCode.Include
    private String languageId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
