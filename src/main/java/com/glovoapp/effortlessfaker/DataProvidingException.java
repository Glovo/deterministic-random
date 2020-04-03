package com.glovoapp.effortlessfaker;

public final class DataProvidingException extends RuntimeException {

    DataProvidingException(final Throwable cause) {
        super("failed to provide data", cause);
    }

}
