package dev.projetos.stefano.order.api.services;

import dev.projetos.stefano.order.api.dtos.request.CategoryRequest;
import dev.projetos.stefano.order.api.dtos.response.CategoryResponse;
import dev.projetos.stefano.order.api.entities.Category;
import dev.projetos.stefano.order.api.mapper.CategoryMapper;
import dev.projetos.stefano.order.api.repositories.CategoryRepository;
import dev.projetos.stefano.order.api.resources.exceptions.DatabaseException;
import dev.projetos.stefano.order.api.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public CategoryResponse insert(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException(id);
            }

            categoryRepository.deleteById(id);
            categoryRepository.flush();
        } catch (DataIntegrityViolationException _) {
            throw new DatabaseException("Cannot delete: Category has associated records");
        }
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(request.name());

            return categoryMapper.toResponse(categoryRepository.save(category));
        } catch (EntityNotFoundException _) {
            throw new ResourceNotFoundException(id);
        }
    }
}
