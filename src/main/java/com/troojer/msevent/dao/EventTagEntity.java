package com.troojer.msevent.dao;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_tag")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "event")
public class EventTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "event_tag_seq")
    private Long id;

    @JoinColumn(name = "event_id")
    @EqualsAndHashCode.Include
    @ManyToOne
    private EventEntity event;

    @Column(name = "tag_id")
    @EqualsAndHashCode.Include
    private Long tagId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
