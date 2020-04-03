package com.glovoapp.effortlessfaker.reflection;

public final class DataClassParametersInitializationException extends RuntimeException {

    DataClassParametersInitializationException(final Class<?> dataClass, final Throwable cause) {
        super("failed to initialize parameters of " + dataClass, cause);
    }

}
