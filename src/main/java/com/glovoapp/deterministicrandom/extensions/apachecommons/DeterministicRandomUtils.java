package com.glovoapp.deterministicrandom.extensions.apachecommons;

import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.RandomUtils;

/**
 * <h1 color="red"> This class is unsafe to use in production environment!</h1><br/>
 * <h2 color="red">Use this class for anything but testing at your own risk.</h2><br/>
 * Injects {@link DeterministicRandom} into {@link RandomUtils}.
 */
public final class DeterministicRandomUtils {

    private static final AtomicBoolean IS_DETERMINISTIC = new AtomicBoolean(false);

    private DeterministicRandomUtils() throws IllegalAccessException {
        throw new IllegalAccessException(
            getClass() + " is a static methods collection of which instances should not be created"
        );
    }

    /**
     * Overwrites the {@link java.util.Random} static field of {@link RandomUtils}. Calling this
     * method will affect methods of {@link RandomUtils} globally. This method may be called only
     * once.
     */
    public static void makeDeterministic() {
        makeDeterministic(new DeterministicRandom());
    }

    /**
     * Overwrites the {@link java.util.Random} static field of {@link RandomUtils}. Calling this
     * method will affect methods of {@link RandomUtils} globally. This method may be called only
     * once.
     */
    public static void makeDeterministic(final DeterministicRandom deterministicRandom) {
        if (!IS_DETERMINISTIC.getAndSet(true)) {
            StaticFieldSetter.makeDeterministic(
                RandomUtils.class,
                "RANDOM",
                deterministicRandom
            );
        } else {
            throw new StaticUtilsClassDeterministicRandomAlreadyInitializedException(
                RandomUtils.class
            );
        }
    }

}
