package com.troojer.msinterest.dao.repository;

import com.troojer.msinterest.dao.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface InterestRepository extends JpaRepository<InterestEntity, Long> {

    Set<InterestEntity> getAllByName(String name);

}
