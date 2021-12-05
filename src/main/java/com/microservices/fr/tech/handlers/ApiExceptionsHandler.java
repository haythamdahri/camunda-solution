package com.microservices.fr.tech.handlers;

import com.microservices.fr.tech.dto.ApiResponse;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestControllerAdvice
public class ApiExceptionsHandler {

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException exception) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // Build API Response
        return ResponseEntity.status(httpStatus).body(
                buildApiResponse(exception, httpStatus)
        );
    }

    @ExceptionHandler(value = {TechnicalException.class})
    public ResponseEntity<ApiResponse> handleTechnicalException(TechnicalException exception) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        // Build API Response
        return ResponseEntity.status(httpStatus).body(
                buildApiResponse(exception, httpStatus)
        );
    }

    private ApiResponse buildApiResponse(Throwable throwable, HttpStatus httpStatus) {
        return ApiResponse.builder()
                .message(throwable.getMessage())
                .httpStatus(httpStatus)
                .statusCode(httpStatus.value())
                .timestamp(LocalDateTime.now(ZoneId.systemDefault()))
                .build();
    }

}
