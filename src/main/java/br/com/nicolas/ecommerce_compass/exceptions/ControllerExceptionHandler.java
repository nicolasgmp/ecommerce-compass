package br.com.nicolas.ecommerce_compass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            EntityValidationException.class,
            DuplicateEntryException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            InvalidRequestException.class
    })
    public ResponseEntity<StandardError> handleBadRequestException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(status.value(), "Bad Request", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status.value(), "Not Found", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> handleMethodNotAllowedException(Exception ex) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        StandardError err = new StandardError(status.value(), "Method Not Allowed", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({
            JWTCreationException.class,
            JWTVerificationException.class
    })
    public ResponseEntity<StandardError> handleUnauthorizedException(Exception ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(status.value(), "Unauthorized", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }
}
