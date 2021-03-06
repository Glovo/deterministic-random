package com.glovoapp.effortlessfaker.reflection;

public final class DataClassHasNoConstructorsException extends RuntimeException {

    DataClassHasNoConstructorsException(final Class<?> dataClass) {
        super("data class " + dataClass + " declares no constructors; this should never happen");
    }

}
