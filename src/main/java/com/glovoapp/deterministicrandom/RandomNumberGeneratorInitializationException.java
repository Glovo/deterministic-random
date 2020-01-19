package com.glovoapp.deterministicrandom;

public final class RandomNumberGeneratorInitializationException extends RuntimeException {

    RandomNumberGeneratorInitializationException(final Throwable cause) {
        super("failed to create an instance of RNG", cause);
    }

}
