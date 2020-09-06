package com.troojer.msevent.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Where(clause = "deleted=0")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "category_seq")
    private Long id;

    private String title;
    private String description;
    @Builder.Default
    private Integer deleted = 0;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
