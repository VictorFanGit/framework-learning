package com.victor.stream.producer;

public class RedisOperationException extends Exception {
    public RedisOperationException(String message) {
        super(message);
    }

    public RedisOperationException(Throwable cause) {
        super(cause);
    }

    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
