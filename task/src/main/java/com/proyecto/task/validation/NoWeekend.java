package com.proyecto.task.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Especifica que esta anotación puede aplicarse sobre campos (fields)
@Target({ElementType.FIELD})
// Indica que la anotación estará disponible en tiempo de ejecución
@Retention(RetentionPolicy.RUNTIME)
// Define que esta anotación es una restricción (validador de Bean Validation)
@Constraint(validatedBy = NoWeekendValidator.class)
public @interface NoWeekend {


    // Mensaje que se mostrará si la validación falla
    String message() default "La fecha no puede caer en fin de semana";

    // Grupos de validación, normalmente se deja vacío si no usás validaciones por grupos
    Class<?>[] groups() default {};

    // Información adicional para los clientes de la API de validación (también suele dejarse vacío)
    Class<? extends Payload>[] payload() default {};
}
