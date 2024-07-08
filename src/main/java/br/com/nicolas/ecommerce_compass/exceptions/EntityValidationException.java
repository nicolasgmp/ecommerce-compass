package br.com.nicolas.ecommerce_compass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityValidationException extends RuntimeException {

    public EntityValidationException(String msg) {
        super(msg);
    }
}
