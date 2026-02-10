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
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CategoryService categoryService, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public ProductResponse insert(ProductRequest request) {
        Product product = productMapper.toEntity(request);

        List<Category> categories = categoryRepository.findAllById(request.categoriesId());

        if (categories.size() != request.categoriesId().size()) {
            throw new ResourceNotFoundException("One or more categories not found");
        }

        product.getCategories().addAll(categories);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (!productRepository.existsById(id)) {
                throw new ResourceNotFoundException(id);
            }

            productRepository.deleteById(id);
            productRepository.flush();
        } catch (DataIntegrityViolationException _) {
            throw new DatabaseException("Cannot delete: Product has associated records");
        }
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        try {
            Product product = productRepository.getReferenceById(id);

            productMapper.updateEntityFromRequest(request, product);

            if (request.categoriesId() != null && !request.categoriesId().isEmpty()) {
                Set<Category> categories = request.categoriesId().stream()
                        .map(categoryService::findEntityById)
                        .collect(Collectors.toSet());

                product.getCategories().clear();
                product.getCategories().addAll(categories);
            }

            return productMapper.toResponse(product);
        } catch (EntityNotFoundException _) {
            throw new ResourceNotFoundException(id);
        }
    }
}
