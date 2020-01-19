package com.glovoapp.deterministicrandom.extensions.datafactory;

import org.fluttercode.datafactory.impl.DataFactory;

public final class GlobalDataFactoryInstanceInitializationException extends RuntimeException {

    GlobalDataFactoryInstanceInitializationException(final Throwable cause) {
        super("failed to initialize global instance of " + DataFactory.class.getName(), cause);
    }

}
