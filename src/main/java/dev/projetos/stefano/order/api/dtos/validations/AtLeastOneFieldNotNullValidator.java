package dev.projetos.stefano.order.api.dtos.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneFieldNotNullValidator implements ConstraintValidator<AtLeastOneFieldNotNull, Object> {
    @Override
    public boolean isValid(Object request, ConstraintValidatorContext constraintValidatorContext) {
        if (request == null) return true;

        for (Field field : request.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(request);

                if (value instanceof String str) {
                    if (!str.isBlank()) {
                        return true;
                    }
                } else if (value != null) {
                    return true;
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}