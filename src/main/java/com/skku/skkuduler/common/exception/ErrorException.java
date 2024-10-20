package com.skku.skkuduler.common.exception;

import lombok.Getter;
@Getter
public class ErrorException extends RuntimeException {
    private final Error error;
    public ErrorException(Error error) {
        super(error.getMsg());
        this.error = error;
    }

}
