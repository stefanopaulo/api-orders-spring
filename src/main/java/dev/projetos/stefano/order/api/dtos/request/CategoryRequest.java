package dev.projetos.stefano.order.api.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Name cannot be empty")
        String name
) {
}
