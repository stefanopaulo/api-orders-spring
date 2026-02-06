package dev.projetos.stefano.order.api.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "E-mail cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone cannot be empty")
        String phone,

        @NotBlank(message = "Password cannot be empty")
        String password) {
}
