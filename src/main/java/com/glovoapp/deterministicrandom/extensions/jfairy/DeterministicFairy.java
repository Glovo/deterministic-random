package com.glovoapp.deterministicrandom.extensions.jfairy;

import static com.glovoapp.deterministicrandom.extensions.DefaultExtensionValues.LOCALE;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.data.DataMaster;
import com.devskiller.jfairy.data.MapBasedDataMaster;
import com.devskiller.jfairy.producer.BaseProducer;
import com.glovoapp.deterministicrandom.DeterministicRandom;
import com.google.inject.Provider;
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
        return Fairy.create(createDataMasterProvider(deterministicRandom), locale);
    }

    private static Provider<DataMaster> createDataMasterProvider(final DeterministicRandom random) {
        return () -> new MapBasedDataMaster(
            new BaseProducer(
                new DeterministicJFairyRandomGenerator(random)
            )
        );
    }

}
