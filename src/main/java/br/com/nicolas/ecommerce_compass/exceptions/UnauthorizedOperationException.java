package br.com.nicolas.ecommerce_compass.exceptions;

public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException(String msg) {
        super(msg);
    }
}
