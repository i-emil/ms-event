package com.troojer.msevent.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_language")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "event_language_seq")
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "language_id")
    private String languageId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
