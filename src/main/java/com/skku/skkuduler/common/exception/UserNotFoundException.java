package com.skku.skkuduler.common.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("user does not exist");
    }
}
