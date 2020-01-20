package com.glovoapp.deterministicrandom.extensions.jfairy;

import com.devskiller.jfairy.Fairy;

public final class FairyInitializationException extends RuntimeException {

    FairyInitializationException(final Throwable cause) {
        super("failed to initialize " + Fairy.class.getName() + " instance", cause);
    }

}
