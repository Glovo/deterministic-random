package com.glovoapp.deterministicrandom.extensions.jfairy;

import static com.glovoapp.deterministicrandom.extensions.DefaultExtensionValues.LOCALE;

import com.devskiller.jfairy.Bootstrap;
import com.devskiller.jfairy.Bootstrap.Builder;
import com.devskiller.jfairy.Fairy;
import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.lang.reflect.Field;
import java.util.Locale;

/**
 * <h1 color="red"> This class is unsafe to use in production environment!</h1><br/>
 * <h2 color="red">Use this class for anything but testing at your own risk.</h2><br/>
 * Provides instances of {@link Fairy} that generate values with {@link DeterministicRandom}.
 */
public final class DeterministicFairy {

    private static final Fairy FAIRY = create();

    /**
     * @return a global instance of deterministic {@link Fairy}
     */
    public static Fairy deterministic() {
        return FAIRY;
    }

    public static Fairy create() {
        return create(LOCALE);
    }

    public static Fairy create(final Locale locale) {
        return create(new DeterministicRandom(), locale);
    }

    public static Fairy create(final DeterministicRandom deterministicRandom) {
        return create(deterministicRandom, LOCALE);
    }

    public static Fairy create(final DeterministicRandom deterministicRandom,
                               final Locale locale) {
        final Builder builder = Bootstrap.builder()
                                         .withLocale(locale);
        injectRandomInto(builder, deterministicRandom);
        return builder.build();
    }

    private static void injectRandomInto(final Builder builder,
                                         final DeterministicRandom deterministicRandom) {
        try {
            final Field randomGeneratorField = builder.getClass()
                                                      .getDeclaredField("randomGenerator");
            randomGeneratorField.setAccessible(true);
            randomGeneratorField.set(builder, new DeterministicJFairyRandomGenerator(
                deterministicRandom
            ));
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new FairyInitializationException(exception);
        }
    }

}
