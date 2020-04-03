package com.glovoapp.effortlessfaker.reflection;

public class DataClassCastingException extends RuntimeException {

    DataClassCastingException(final Class<?> dataClass, final Throwable cause) {
        super("failed to cast data to " + dataClass, cause);
    }

}
