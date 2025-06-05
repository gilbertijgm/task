package com.proyecto.task.validation;

import com.proyecto.task.advice.GlobalExceptionHandler;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DayOfWeek;
import java.time.LocalDate;

// Implementa el validador para campos de tipo LocalDate con la anotación
public class NoWeekendValidator implements ConstraintValidator<NoWeekend, LocalDate> {


    // Inicialización del validador (no lo usamos en este caso)
    @Override
    public void initialize(NoWeekend constraintAnnotation) {
        // Nada que inicializar
    }


    // Método principal que realiza la validación
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        // Si el campo es null, dejamos que otra anotación como @NotNull lo maneje
        if (date == null) return true;

        // Obtenemos el día de la semana de la fecha
        DayOfWeek day = date.getDayOfWeek();

        // Retornamos true solo si no es sábado ni domingo
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }


}
