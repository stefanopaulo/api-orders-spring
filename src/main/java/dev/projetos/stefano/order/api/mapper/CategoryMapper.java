package dev.projetos.stefano.order.api.mapper;

import dev.projetos.stefano.order.api.dtos.request.CategoryRequest;
import dev.projetos.stefano.order.api.dtos.response.CategoryResponse;
import dev.projetos.stefano.order.api.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return category;
    }

}
