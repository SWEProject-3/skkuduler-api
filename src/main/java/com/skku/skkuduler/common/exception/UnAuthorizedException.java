package com.skku.skkuduler.common.exception;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
        super("you cannot access user");
    }
}
