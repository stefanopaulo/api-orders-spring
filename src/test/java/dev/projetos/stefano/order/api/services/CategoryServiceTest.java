package dev.projetos.stefano.order.api.services;

import dev.projetos.stefano.order.api.dtos.request.CategoryRequest;
import dev.projetos.stefano.order.api.dtos.response.CategoryResponse;
import dev.projetos.stefano.order.api.entities.Category;
import dev.projetos.stefano.order.api.mapper.CategoryMapper;
import dev.projetos.stefano.order.api.repositories.CategoryRepository;
import dev.projetos.stefano.order.api.resources.exceptions.DatabaseException;
import dev.projetos.stefano.order.api.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findById_whenCategoryExists_shouldReturnCategoryResponse() {
        // Given
        Category cat = new Category(1L, "Electronics");
        CategoryResponse expectedResponse = new CategoryResponse(1L, "Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(categoryMapper.toResponse(cat)).thenReturn(expectedResponse);

        // When
        CategoryResponse result = categoryService.findById(1L);

        // Then
        assertEquals(result, expectedResponse);
        assertEquals(1L, result.id());
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toResponse(cat);
    }

    @Test
    void findById_whenCategoryNotFound_shouldThrowResourceNotFoundException() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(1L));
        verify(categoryRepository).findById(1L);
        verify(categoryMapper, never()).toResponse(any());
    }

    @Test
    void findAll_whenCategoriesExists_shouldReturnListOfCategoryResponse() {
        // Given
        Category cat1 = new Category(1L, "Electronics");
        Category cat2 = new Category(2L, "Books");
        Category cat3 = new Category(3L, "Computers");

        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2, cat3));

        CategoryResponse resp1 = new CategoryResponse(1L, "Electronics");
        CategoryResponse resp2 = new CategoryResponse(2L, "Books");
        CategoryResponse resp3 = new CategoryResponse(3L, "Computers");

        when(categoryMapper.toResponse(cat1)).thenReturn(resp1);
        when(categoryMapper.toResponse(cat2)).thenReturn(resp2);
        when(categoryMapper.toResponse(cat3)).thenReturn(resp3);

        // When
        List<CategoryResponse> responses = categoryService.findAll();

        // Then
        assertEquals(3, responses.size());
        verify(categoryRepository).findAll();
        verify(categoryMapper, times(3)).toResponse(any());
    }

    @Test
    void findAll_whenNoCategoriesExists_shouldReturnEmptyList() {
        // Given
        when(categoryRepository.findAll()).thenReturn(List.of());

        // When
        List<CategoryResponse> responses = categoryService.findAll();

        // Then
        assertTrue(responses.isEmpty());
        verify(categoryRepository).findAll();
        verify(categoryMapper, never()).toResponse(any());
    }

    @Test
    void insert_whenValidRequest_shouldSaveAndReturnCategoryResponse() {
        // Given
        Category savedCategory = new Category(1L, "Electronics");
        CategoryRequest request = new CategoryRequest("Electronics");
        CategoryResponse response = new CategoryResponse(1L, "Electronics");
        Category categoryWithoutId = new Category(null, "Electronics");

        when(categoryMapper.toEntity(request)).thenReturn(categoryWithoutId);
        when(categoryRepository.save(categoryWithoutId)).thenReturn(savedCategory);
        when(categoryMapper.toResponse(savedCategory)).thenReturn(response);

        // When
        CategoryResponse result = categoryService.insert(request);

        // Then
        assertEquals(result, response);
        verify(categoryMapper).toEntity(request);
        verify(categoryRepository, times(1)).save(categoryWithoutId);
        verify(categoryMapper).toResponse(savedCategory);
    }

    @Test
    void delete_whenCategoryExists_shouldDeleteSuccessfully() {
        // Given
        Long catId = 1L;

        when(categoryRepository.existsById(catId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(catId);

        // When
        categoryService.delete(catId);

        // Then
        verify(categoryRepository).existsById(catId);
        verify(categoryRepository).deleteById(catId);
    }

    @Test
    void delete_whenCategoryNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long catId = 999L;

        when(categoryRepository.existsById(catId)).thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(catId));

        verify(categoryRepository).existsById(catId);
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenCategoryHasAssociatedRecords_shouldThrowDatabaseException() {
        // Given
        Long catId = 1L;

        when(categoryRepository.existsById(catId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).deleteById(catId);

        // When
        DatabaseException exception = assertThrows(DatabaseException.class, () -> categoryService.delete(catId));

        // Then
        assertEquals("Cannot delete: Category has associated records", exception.getMessage());
        verify(categoryRepository).existsById(catId);
        verify(categoryRepository).deleteById(catId);
    }

    @Test
    void update_whenCategoryExists_shouldUpdateAndReturnUserResponse() {
        // Given
        Long catId = 1L;
        CategoryRequest request = new CategoryRequest("Food");

        Category existingCat = new Category(1L, "Electronics");
        Category updatedCat = new Category(1L, request.name());
        CategoryResponse resp = new CategoryResponse(1L, updatedCat.getName());

        when(categoryRepository.getReferenceById(catId)).thenReturn(existingCat);
        when(categoryRepository.save(existingCat)).thenReturn(updatedCat);
        when(categoryMapper.toResponse(updatedCat)).thenReturn(resp);

        // When
        CategoryResponse result = categoryService.update(catId, request);

        // Then
        assertEquals(result, resp);
        verify(categoryRepository).getReferenceById(catId);
        verify(categoryRepository, times(1)).save(existingCat);
        verify(categoryMapper).toResponse(updatedCat);
    }

    @Test
    void update_whenCategoryNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long catId = 999L;

        CategoryRequest request = new CategoryRequest("Food");

        when(categoryRepository.getReferenceById(catId)).thenThrow(EntityNotFoundException.class);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(catId, request));
        verify(categoryRepository, never()).save(any());
        verify(categoryMapper, never()).toResponse(any());
    }

}
