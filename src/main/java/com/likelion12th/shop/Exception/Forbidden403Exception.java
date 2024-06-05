package com.likelion12th.shop.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.FORBIDDEN)
public class Forbidden403Exception extends RuntimeException {

    public Forbidden403Exception(String message) {
        super(message);
    }
}
