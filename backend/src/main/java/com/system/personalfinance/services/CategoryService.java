package com.system.personalfinance.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.personalfinance.dtos.category.CreateCategoryRequest;
import com.system.personalfinance.dtos.category.UpdateCategoryRequest;
import com.system.personalfinance.entities.Category;
import com.system.personalfinance.entities.User;
import com.system.personalfinance.exceptions.ConflictException;
import com.system.personalfinance.exceptions.NotFoundException;
import com.system.personalfinance.repositories.CategoryRepository;
import com.system.personalfinance.repositories.UserRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> list(Long userId) {
        return categoryRepository.findAllByUserIdOrderByNameAsc(userId);
    }

    @Transactional
    public Category create(Long userId, CreateCategoryRequest request) {
        if (categoryRepository.existsByUserIdAndNameIgnoreCaseAndType(userId, request.name(), request.type())) {
            throw new ConflictException("Categoria já existe para esse tipo.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        Category c = new Category();
        c.setUser(user);
        c.setName(request.name().trim());
        c.setType(request.type());

        return categoryRepository.save(c);
    }

    @Transactional(readOnly = true)
    public Category getById(Long userId, Long id) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
    }

    @Transactional
    public Category update(Long userId, Long id, UpdateCategoryRequest request) {
        Category c = getById(userId, id);

        boolean conflict = categoryRepository.existsByUserIdAndNameIgnoreCaseAndType(userId, request.name(), request.type())
                && !(c.getName().equalsIgnoreCase(request.name()) && c.getType() == request.type());

        if (conflict) {
            throw new ConflictException("Categoria já existe para esse tipo.");
        }

        c.setName(request.name().trim());
        c.setType(request.type());

        return categoryRepository.save(c);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        Category c = getById(userId, id);
        categoryRepository.delete(c);
    }
}
