package com.example.gotogether.category.repository;

import com.example.gotogether.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentIsNull();

    boolean existsByName(String name);

    List<Category> findAllByCategoryIdIn(List<Long> list);
}