package com.troojer.msevent.dao;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq_generator")
    @SequenceGenerator(name = "event_seq_generator", sequenceName = "event_seq")
    @EqualsAndHashCode.Include
    private Long id;

    private Long eventId;

    private String userId;

    private String text;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
