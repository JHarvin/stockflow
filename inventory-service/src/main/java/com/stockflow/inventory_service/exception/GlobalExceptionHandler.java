package com.stockflow.inventory_service.exception;

import com.stockflow.inventory_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        var error = new ErrorResponse(LocalDateTime.now(), 500, "Internal Server Error", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }// fin clase

    @ExceptionHandler(io.github.resilience4j.ratelimiter.RequestNotPermitted.class)
    public ResponseEntity<ErrorResponse> handleRateLimit(io.github.resilience4j.ratelimiter.RequestNotPermitted ex, HttpServletRequest req) {
        var error = new ErrorResponse(
                LocalDateTime.now(),
                429,
                "Too Many Requests",
                "Has excedido el límite de 10 peticiones por segundo",
                req.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }


}
