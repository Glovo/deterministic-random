package com.glovoapp.deterministicrandom.extensions.apachecommons;

public final class StaticUtilsClassDeterministicRandomInitializationException
    extends RuntimeException {

    StaticUtilsClassDeterministicRandomInitializationException(final Class<?> utilsClass,
                                                               final Throwable cause) {
        super("failed to initialize deterministic random in " + utilsClass.getName(), cause);
    }

}
