package com.devsuperior.desafio.services.exceptions;

public class ExceptionBadRequest extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ExceptionBadRequest(String msg) {
        super(msg);
    }
}
