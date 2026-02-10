package dev.projetos.stefano.order.api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ProductRequest(

        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Price cannot be empty")
        @Positive(message = "Price must be positive")
        Double price,

        String imgUrl,

        @NotEmpty(message = "At least one category is required")
        List<Long> categoriesId
) {
}
