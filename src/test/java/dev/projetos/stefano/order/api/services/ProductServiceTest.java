package dev.projetos.stefano.order.api.services;

import dev.projetos.stefano.order.api.dtos.request.ProductRequest;
import dev.projetos.stefano.order.api.dtos.request.ProductUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.ProductResponse;
import dev.projetos.stefano.order.api.entities.Category;
import dev.projetos.stefano.order.api.entities.Product;
import dev.projetos.stefano.order.api.mapper.ProductMapper;
import dev.projetos.stefano.order.api.repositories.CategoryRepository;
import dev.projetos.stefano.order.api.repositories.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void findById_whenProductExists_shouldReturnProductResponse() {
        // Given
        Product product = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        ProductResponse expectedResponse = new ProductResponse(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "", List.of());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        // When
        ProductResponse result = productService.findById(1L);

        // Then
        assertEquals(result, expectedResponse);
        assertEquals(1L, result.id());
        verify(productRepository).findById(1L);
        verify(productMapper).toResponse(product);
    }

    @Test
    void findById_whenProductNotFound_shouldThrowResourceNotFoundException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(1L));
        verify(productRepository).findById(1L);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void findAll_whenCategoriesExists_shouldReturnListOfProductResponse() {
        // Given
        Product p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        Product p2 = new Product(2L, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
        Product p3 = new Product(3L, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "");

        when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        ProductResponse resp1 = new ProductResponse(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "", List.of());
        ProductResponse resp2 = new ProductResponse(2L, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "", List.of());
        ProductResponse resp3 = new ProductResponse(3L, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "", List.of());

        when(productMapper.toResponse(p1)).thenReturn(resp1);
        when(productMapper.toResponse(p2)).thenReturn(resp2);
        when(productMapper.toResponse(p3)).thenReturn(resp3);

        // When
        List<ProductResponse> responses = productService.findAll();

        // Then
        assertEquals(3, responses.size());
        assertEquals(resp1, responses.get(0));
        assertEquals(resp2, responses.get(1));
        assertEquals(resp3, responses.get(2));
        verify(productRepository).findAll();
        verify(productMapper, times(3)).toResponse(any());
    }

    @Test
    void findAll_whenNoCategoriesExists_shouldReturnEmptyList() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of());

        // When
        List<ProductResponse> responses = productService.findAll();

        // Then
        assertTrue(responses.isEmpty());
        verify(productRepository).findAll();
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void insert_whenValidRequest_shouldSaveAndReturnProductResponse() {
        // Given
        Product savedProduct = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        ProductRequest request = new ProductRequest("The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "", List.of(2L));
        ProductResponse response = new ProductResponse(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "", List.of());
        Product productWithoutId = new Product(null, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        Category cat = new Category(2L, "Books");

        when(productMapper.toEntity(request)).thenReturn(productWithoutId);
        when(categoryRepository.findAllById(request.categoriesId())).thenReturn(List.of(cat));
        when(productRepository.save(productWithoutId)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(response);

        // When
        ProductResponse result = productService.insert(request);

        // Then
        assertEquals(result, response);
        verify(productMapper).toEntity(request);
        verify(categoryRepository).findAllById(request.categoriesId());
        verify(productRepository, times(1)).save(productWithoutId);
        verify(productMapper).toResponse(savedProduct);
    }

    @Test
    void insert_whenValidRequest_shouldThrowResourceNotFoundException() {
        // Given
        ProductRequest request = new ProductRequest("The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "", List.of(2L));
        Product productWithoutId = new Product(null, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");

        when(productMapper.toEntity(request)).thenReturn(productWithoutId);
        when(categoryRepository.findAllById(request.categoriesId())).thenReturn(List.of());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productService.insert(request));
        verify(productMapper).toEntity(request);
        verify(categoryRepository).findAllById(request.categoriesId());
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void delete_whenProductExists_shouldDeleteSuccessfully() {
        // Given
        Long prodId = 1L;

        when(productRepository.existsById(prodId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(prodId);

        // When
        productService.delete(prodId);

        // Then
        verify(productRepository).existsById(prodId);
        verify(productRepository).deleteById(prodId);
        verify(productRepository).flush();
    }

    @Test
    void delete_whenProductNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long prodId = 999L;

        when(productRepository.existsById(prodId)).thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(prodId));

        verify(productRepository).existsById(prodId);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenProductHasAssociatedRecords_shouldThrowDatabaseException() {
        // Given
        Long prodId = 1L;

        when(productRepository.existsById(prodId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(prodId);

        // When
        DatabaseException exception = assertThrows(DatabaseException.class, () -> productService.delete(prodId));

        // Then
        assertEquals("Cannot delete: Product has associated records", exception.getMessage());
        verify(productRepository).existsById(prodId);
        verify(productRepository).deleteById(prodId);
    }

    @Test
    void update_whenProductExists_shouldUpdateAndReturnUserResponse() {
        // Given
        Long prodId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest("Harry Potter and The Philosopher's Stone", "Lorem ipsum dolor sit amet, consectetur.", 112.5, "", List.of(2L));

        Product existingProd = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        Product updatedProd = new Product(1L, request.name(), "Lorem ipsum dolor sit amet, consectetur.", request.price(), "");
        ProductResponse resp = new ProductResponse(1L, updatedProd.getName(), "Lorem ipsum dolor sit amet, consectetur.", updatedProd.getPrice(), "", List.of());

        Category cat = new Category(2L, "Books");

        when(productRepository.getReferenceById(prodId)).thenReturn(existingProd);
        when(productMapper.toResponse(updatedProd)).thenReturn(resp);
        when(categoryService.findEntityById(2L)).thenReturn(cat);

        // When
        ProductResponse result = productService.update(prodId, request);

        // Then
        assertEquals(result, resp);
        verify(productRepository).getReferenceById(prodId);
        verify(productMapper).toResponse(existingProd);
    }

    @Test
    void update_whenProductNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long prodId = 999L;

        ProductUpdateRequest request = new ProductUpdateRequest("Harry Potter and The Philosopher's Stone", "Lorem ipsum dolor sit amet, consectetur.", 112.5, "", List.of(2L));

        when(productRepository.getReferenceById(prodId)).thenThrow(EntityNotFoundException.class);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productService.update(prodId, request));
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponse(any());
    }
}
