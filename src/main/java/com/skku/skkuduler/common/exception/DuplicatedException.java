package com.skku.skkuduler.common.exception;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException() {
        super("duplicated entry");
    }
    public DuplicatedException(String message) {super(message);}
}
