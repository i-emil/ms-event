package com.troojer.msevent.dao.repository;

import com.troojer.msevent.dao.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
