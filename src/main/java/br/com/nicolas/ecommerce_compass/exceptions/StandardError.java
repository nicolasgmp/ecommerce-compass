package br.com.nicolas.ecommerce_compass.exceptions;

public class StandardError {

    private int code;
    private String status;
    private String message;

    public StandardError(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
