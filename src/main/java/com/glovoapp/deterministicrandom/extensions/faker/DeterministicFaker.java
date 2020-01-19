package com.glovoapp.deterministicrandom.extensions.faker;

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

    /**
     * @return a global instance of deterministic {@link Faker}
     */
    public static Faker deterministic() {
        return FAKER;
    }

    public static Faker create() {
        return create(Locale.ENGLISH);
    }

    public static Faker create(final Locale locale) {
        return create(locale, new DeterministicRandom());
    }

    public static Faker create(final Locale locale, final DeterministicRandom deterministicRandom) {
        return new Faker(locale, deterministicRandom);
    }

}
