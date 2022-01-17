package com.backend.hallodos.exceptions;

public class AuthFailException extends IllegalArgumentException{
    public AuthFailException(String msg) {
        super(msg);
    }
}
