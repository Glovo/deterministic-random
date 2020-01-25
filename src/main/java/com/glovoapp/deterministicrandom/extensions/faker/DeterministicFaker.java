package com.glovoapp.deterministicrandom.extensions.faker;

import static com.glovoapp.deterministicrandom.extensions.DefaultExtensionValues.LOCALE;

import com.github.javafaker.Faker;
import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.util.Locale;

/**
 * <h1 color="red"> This class is unsafe to use in production environment!</h1><br/>
 * <h2 color="red">Use this class for anything but testing at your own risk.</h2><br/>
 * Provides instances of {@link Faker} that generate values with {@link DeterministicRandom}.
 */
public final class DeterministicFaker extends Faker {

    private static final Faker FAKER = create();

    private DeterministicFaker() throws IllegalAccessException {
        throw new IllegalAccessException(
            getClass() + " is a static methods collection of which instances should not be created"
        );
    }

    /**
     * @return a global instance of deterministic {@link Faker}
     */
    public static Faker deterministic() {
        return FAKER;
    }

    public static Faker create() {
        return create(LOCALE);
    }

    public static Faker create(final Locale locale) {
        return create(new DeterministicRandom(), locale);
    }

    public static Faker create(final DeterministicRandom deterministicRandom) {
        return create(deterministicRandom, LOCALE);
    }

    public static Faker create(final DeterministicRandom deterministicRandom,
                               final Locale locale) {
        return new Faker(locale, deterministicRandom);
    }

}
