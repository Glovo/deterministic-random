package com.glovoapp.effortlessfaker.reflection;

public final class DataClassNotInheritedFromObjectException extends RuntimeException {

    DataClassNotInheritedFromObjectException(final Class<?> actualClass) {
        super("data class must be derived from any other class, found " + actualClass);
    }

}