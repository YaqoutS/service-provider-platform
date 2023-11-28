package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
