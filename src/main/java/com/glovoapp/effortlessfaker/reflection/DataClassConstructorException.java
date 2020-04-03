package com.glovoapp.effortlessfaker.reflection;

public final class DataClassConstructorException extends RuntimeException {

    DataClassConstructorException(final Throwable cause) {
        super("failed to provide data using constructor", cause);
    }

}
