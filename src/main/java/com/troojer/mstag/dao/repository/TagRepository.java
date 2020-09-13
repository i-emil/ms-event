package com.troojer.mstag.dao.repository;

import com.troojer.mstag.dao.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> getByValueEquals(String value);

    Page<TagEntity> getByValueStartsWith(String value, Pageable pageable);

    Set<TagEntity> getAllByValueIn(Collection<String> value);

}
