package dev.projetos.stefano.order.api.dtos.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldNotNullValidator.class)
public @interface AtLeastOneFieldNotNull {
    String message() default "At least one field must be provided for update";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
