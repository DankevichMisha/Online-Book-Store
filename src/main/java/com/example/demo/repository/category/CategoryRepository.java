package com.example.demo.repository.category;

import com.example.demo.model.Category;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
    Set<Category> findByIdIn(Set<Long> ids);
}
