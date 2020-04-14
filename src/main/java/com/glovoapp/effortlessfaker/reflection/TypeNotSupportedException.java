package com.glovoapp.effortlessfaker.reflection;

import java.lang.reflect.Type;

public final class TypeNotSupportedException extends RuntimeException {

    TypeNotSupportedException(final String name, final Type type) {
        super("field of name " + name + " and type " + type + " is not supported");
    }

}
