package br.com.nicolas.ecommerce_compass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleForbiddenException(Exception ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(status.value(), "Forbidden", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MailServiceException.class)
    public ResponseEntity<StandardError> handleInternalServerException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(status.value(), "Internal Server Error", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }

}
