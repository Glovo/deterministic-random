package com.glovoapp.effortlessfaker;

public final class MissingDataProviderException extends RuntimeException {

    MissingDataProviderException(final Class<?> fieldClass) {
        super("missing provider for field with type " + fieldClass);
    }

}
