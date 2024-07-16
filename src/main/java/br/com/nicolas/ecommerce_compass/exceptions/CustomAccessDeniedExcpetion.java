package br.com.nicolas.ecommerce_compass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomAccessDeniedExcpetion extends RuntimeException {
    public CustomAccessDeniedExcpetion(String msg) {
        super(msg);
    }
}
