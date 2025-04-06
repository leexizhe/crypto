package org.crypto.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEnumException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType.isEnum()) {
                String acceptedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid value provided. Accepted values: [" + acceptedValues + "]");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid request format"));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handlePriceNotFoundException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
