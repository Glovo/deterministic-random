package com.glovoapp.deterministicrandom.extensions.apachecommons;

public class StaticUtilsClassDeterministicRandomAlreadyInitializedException
    extends RuntimeException {

    StaticUtilsClassDeterministicRandomAlreadyInitializedException(final Class<?> utilsClass) {
        super("class " + utilsClass.getName() + " has already been initialized");
    }

}
