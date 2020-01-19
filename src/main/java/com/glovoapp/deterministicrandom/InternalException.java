package com.glovoapp.deterministicrandom;

final class InternalException extends RuntimeException {

    InternalException() {
        super("this should never happen but if it does, please raise an issue in GitHub");
    }

}
