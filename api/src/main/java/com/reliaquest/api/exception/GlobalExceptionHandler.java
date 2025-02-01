package com.reliaquest.api.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.EmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(EmployeeChallengeException.class)
    protected ResponseEntity<?> handleException(EmployeeChallengeException ex) {
        log.error("Application Exception", ex);
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponse(ex.getMessage(), null, null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleException(MethodArgumentTypeMismatchException ex) {
        log.error("Application Exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getCause().getMessage(), null, null));
    }


    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    protected ResponseEntity<?> handleException(HttpClientErrorException.TooManyRequests ex) {
        String errMsg = "Too many requests, Please try again later";
        log.error(errMsg, ex);
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(errMsg, null, null));
    }

    @SneakyThrows
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<?> handleException(HttpClientErrorException ex) {
        String errMsg = "Server Exception";
        log.error(errMsg, ex);
        EmployeeResponseDto<String> response = objectMapper.readValue(ex.getResponseBodyAsByteArray(), new TypeReference<>() {});
        if(response != null) {
            //TODO When server sends correct error in the response use response.error() instead.
            errMsg = response.status();
        }
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(errMsg, null, null));
    }

    @SneakyThrows
    @ExceptionHandler(HttpServerErrorException.class)
    protected ResponseEntity<?> handleException(HttpServerErrorException ex) {
        String errMsg = "Server Exception";
        log.error(errMsg, ex);
        EmployeeResponseDto<String> response = objectMapper.readValue(ex.getResponseBodyAsByteArray(), new TypeReference<>() {});
        if(response != null) {
            //TODO When server sends correct error in the response use response.error() instead.
            errMsg = response.status();
        }
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(errMsg, null, null));
    }

    @ExceptionHandler(ResourceAccessException.class)
    protected ResponseEntity<?> handleException(ResourceAccessException ex) {
        log.error("Server Exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Employee data service is unavailable", null, null));
    }

    @ExceptionHandler
    protected ResponseEntity<?> handleException(Throwable ex) {
        log.error("Unhandled Exception", ex);
        return ResponseEntity.internalServerError().body(new ErrorResponse(ex.getMessage(),null, null));
    }
}
