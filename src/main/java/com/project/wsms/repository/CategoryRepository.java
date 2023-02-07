package com.project.wsms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    List<Category> findByCateNameContains(String cateName);
    Category findByCateName(String cateName);
}
