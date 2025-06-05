package com.proyecto.task.advice;

import com.proyecto.task.response.ApiResponse;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* MANEJO DE ERRORES DE VALIDACION
     * crear un metodo de tipo map, porque vamos a devolver un json y la estructura del json es un mapa clave valor
     *
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArguments(MethodArgumentNotValidException exception){
        //defino el mapa
        Map<String, String> errors = new HashMap<>();

        /*
         * getBindingResult() muestra el resultado de la exception
         * getFieldErrors() muestra los errors de todos los campos
         *
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            //con put le seteamos todos los campos(clave) y el mensage(valor) al mapa
            errors.put(error.getField(), error.getDefaultMessage());
        });

        //retorno el mapa errors
        return errors;
    }

    // Manejo de errores de parseo JSON (como enums vacíos o inválidos)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();

        if (ex.getMessage() != null && ex.getMessage().contains("Task$Status")) {
            error.put("error", "El estado enviado es inválido o está vacío. Valores permitidos: Pendiente, Desarrollo, Completada, Cancelada.");
        } else {
            error.put("error", "Error en el formato del JSON. Verificá los datos enviados.");
        }

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        response.put("path", getCurrentRequestPath());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        response.put("path", getCurrentRequestPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", "Ocurrió un error inesperado.");
        response.put("path", getCurrentRequestPath());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(ex.getReason()) {
        });
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, Object>> handleMissingPathVariableException(MissingPathVariableException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "El parámetro de la URL está ausente.");
        response.put("path", getCurrentRequestPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getCurrentRequestPath() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest().getRequestURI() : "Desconocido";
    }
    */

    /**
     * Manejador para errores de validación de campos (anotaciones como @NotNull, @Size, etc.).
     * Devuelve una lista de errores en el campo "data".
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Extraemos y formateamos los errores campo por campo
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Errores de validación",
                errores
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Manejador para errores de JSON mal formateado o valores inválidos (como enums no reconocidos).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        String mensaje;

        if (ex.getMessage() != null && ex.getMessage().contains("Task$Status")) {
            mensaje = "El estado enviado es inválido o está vacío. Valores permitidos: Pendiente, Desarrollo, Completada, Cancelada.";
        } else {
            mensaje = "Error en el formato del JSON. Verificá los datos enviados.";
        }

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                mensaje,
                null
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Manejador para recursos no encontrados. Lanza tu excepción personalizada.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Manejador para argumentos inválidos que no pasaron por validaciones con anotaciones.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manejador para excepciones que lanzás con ResponseStatusException manualmente.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Object>> handleResponseStatusException(ResponseStatusException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ex.getStatusCode().value(),
                ex.getReason(),
                null
        );

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    /**
     * Manejador cuando un @PathVariable esperado no fue enviado en la URL.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingPathVariableException(MissingPathVariableException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "El parámetro de la URL está ausente.",
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manejador global para errores no controlados. Siempre útil tener uno general por seguridad.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        // Podés loguearlo aquí si querés
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado.",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
