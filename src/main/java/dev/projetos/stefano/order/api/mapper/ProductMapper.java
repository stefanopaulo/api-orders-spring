package dev.projetos.stefano.order.api.mapper;

import dev.projetos.stefano.order.api.dtos.request.ProductRequest;
import dev.projetos.stefano.order.api.dtos.request.ProductUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.ProductResponse;
import dev.projetos.stefano.order.api.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                product.getCategories()
                        .stream()
                        .map(categoryMapper::toResponse)
                        .toList()
        );
    }

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImgUrl(request.imgUrl());

        return product;
    }

    public void updateEntityFromRequest(ProductUpdateRequest request, Product entity) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.price() != null) entity.setPrice(request.price());
        if (request.imgUrl() != null) entity.setImgUrl(request.imgUrl());
    }
}
