package dev.projetos.stefano.order.api.dtos.request;

import dev.projetos.stefano.order.api.dtos.validations.AtLeastOneFieldNotNull;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

@AtLeastOneFieldNotNull
public record UserUpdateRequest (
        @Length(min = 1, message = "Name cannot be empty")
        String name,

        @Email(message = "Invalid email format")
        String email,

        @Length(min = 1, message = "Phone cannot be empty")
        String phone
) {

}
