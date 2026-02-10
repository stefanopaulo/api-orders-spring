package dev.projetos.stefano.order.api.dtos.request;

import dev.projetos.stefano.order.api.dtos.validations.AtLeastOneFieldNotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@AtLeastOneFieldNotNull
public record ProductUpdateRequest(
        @Length(min = 1, message = "Name cannot be empty")
        String name,

        @Length(min = 1, message = "Description cannot be empty")
        String description,

        @Positive(message = "Price must be positive")
        Double price,

        String imgUrl,

        List<@Positive Long> categoriesId
) {
}
