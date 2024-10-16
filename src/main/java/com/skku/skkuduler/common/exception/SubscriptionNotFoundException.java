package com.skku.skkuduler.common.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException() {
        super("subscription does not exist");
    }
}
