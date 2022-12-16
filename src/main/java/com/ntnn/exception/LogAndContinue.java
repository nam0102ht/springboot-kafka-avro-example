package com.ntnn.exception;

public abstract class LogAndContinue  extends RuntimeException {
    public LogAndContinue(String msg) {
        super(msg);
    }
    public LogAndContinue(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
