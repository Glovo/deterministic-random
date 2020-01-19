package com.glovoapp.deterministicrandom.extensions.datafactory;

import org.fluttercode.datafactory.impl.DataFactory;

public final class GlobalDataFactoryInstanceAlreadyExistsException extends RuntimeException {

    GlobalDataFactoryInstanceAlreadyExistsException() {
        super(
            "cannot create a new global instance of " + DataFactory.class.getName()
                + "because one already exists"
        );
    }

}
