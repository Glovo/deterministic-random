package com.glovoapp.effortlessfaker.reflection;

public final class DataClassParameterProviderInitializationException extends RuntimeException {

    DataClassParameterProviderInitializationException(final String parameterName,
                                                      final Class<?> parameterClass,
                                                      final Throwable cause) {
        super("failed to initialize parameter named " + parameterName + " of type " + parameterClass, cause);
    }

}
