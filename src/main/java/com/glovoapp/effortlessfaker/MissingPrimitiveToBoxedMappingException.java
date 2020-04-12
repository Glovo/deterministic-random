package com.glovoapp.effortlessfaker;

public final class MissingPrimitiveToBoxedMappingException extends RuntimeException {

    MissingPrimitiveToBoxedMappingException(final Class<?> primitiveType) {
        super("mapping for " + primitiveType + " is missing");
    }

}
