package com.example.backend;

public class ErrorResponse {

    public Integer status;

    public String message;

    public String path;

    public ErrorResponse() {
    }

    public ErrorResponse(Integer status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
