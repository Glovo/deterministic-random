package com.glovoapp.deterministicrandom.extensions.apachecommons;

import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * <h1 color="red"> This class is unsafe to use in production environment!</h1><br/>
 * <h2 color="red">Use this class for anything but testing at your own risk.</h2><br/>
 * Injects {@link DeterministicRandom} into {@link RandomStringUtils}.
 */
public final class DeterministicRandomStringUtils {

    private static final AtomicBoolean IS_DETERMINISTIC = new AtomicBoolean(false);

    /**
     * Overwrites the {@link java.util.Random} static field of {@link RandomStringUtils}. Calling
     * this method will affect methods of {@link RandomStringUtils} globally. This method may be
     * called only once.
     */
    public static void makeDeterministic() {
        makeDeterministic(new DeterministicRandom());
    }

    /**
     * Overwrites the {@link java.util.Random} static field of {@link RandomStringUtils}. Calling
     * this method will affect methods of {@link RandomStringUtils} globally. This method may be
     * called only once.
     */
    public static void makeDeterministic(final DeterministicRandom deterministicRandom) {
        if (!IS_DETERMINISTIC.getAndSet(true)) {
            StaticFieldSetter.makeDeterministic(
                RandomStringUtils.class,
                "RANDOM",
                deterministicRandom
            );
        } else {
            throw new StaticUtilsClassDeterministicRandomAlreadyInitializedException(
                RandomStringUtils.class
            );
        }
    }

}
