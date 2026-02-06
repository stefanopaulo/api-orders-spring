package dev.projetos.stefano.order.api.dtos.validations;

import dev.projetos.stefano.order.api.dtos.request.UserUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldNotNullValidator implements ConstraintValidator<AtLeastOneFieldNotNull, UserUpdateRequest> {
    @Override
    public boolean isValid(UserUpdateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request == null) return true;

        return request.name() != null ||
                request.email() != null ||
                request.phone() != null;
    }
}
