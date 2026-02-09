package com.system.personalfinance.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.personalfinance.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserIdOrderByNameAsc(Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndNameIgnoreCaseAndType(Long userId, String name,
            com.system.personalfinance.enums.TransactionTypeEnum type);
}
