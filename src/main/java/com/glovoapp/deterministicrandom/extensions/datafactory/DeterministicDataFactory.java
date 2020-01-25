package com.glovoapp.deterministicrandom.extensions.datafactory;

import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.fluttercode.datafactory.impl.DataFactory;

/**
 * <h1 color="red"> This class is unsafe to use in production environment!</h1><br/>
 * <h2 color="red">Use this class for anything but testing at your own risk.</h2><br/>
 * Provides a global instance of {@link DataFactory} that generate values with {@link
 * DeterministicRandom}. Due to the limitations in extensibility of {@link DataFactory}, only one
 * instance may exist at a time. See {@link #createDataFactory(DeterministicRandom)} for more
 * information about this issue.
 */
public final class DeterministicDataFactory {

    private static final Logger LOG = Logger.getLogger(DeterministicDataFactory.class.getName());
    private static final AtomicReference<DataFactory> DATA_FACTORY = new AtomicReference<>(null);

    private DeterministicDataFactory() throws IllegalAccessException {
        throw new IllegalAccessException(
            getClass() + " is a static methods collection of which instances should not be created"
        );
    }

    /**
     * Creates a global instance of {@link DataFactory}. Only one instance may exist at a time, see
     * {@link #createDataFactory(DeterministicRandom)} for ore information about this issue.
     *
     * @return a new {@link DataFactory} that generate values with {@link DeterministicRandom}
     */
    public static DataFactory create() {
        return create(new DeterministicRandom());
    }

    /**
     * Creates a global instance of {@link DataFactory}. Only one instance may exist at a time, see
     * {@link #createDataFactory(DeterministicRandom)} for ore information about this issue.
     *
     * @param deterministicRandom to be used for generating values
     * @return a new {@link DataFactory} that generate values with given {@link DeterministicRandom}
     */
    public static DataFactory create(final DeterministicRandom deterministicRandom) {
        return DATA_FACTORY.updateAndGet(currentFactory -> {
            if (currentFactory != null) {
                throw new GlobalDataFactoryInstanceAlreadyExistsException();
            } else {
                return createDataFactory(deterministicRandom);
            }
        });
    }

    /**
     * Creates a new instance of {@link DataFactory} that uses given {@link DeterministicRandom} for
     * generating values. This method must be used at most once, otherwise the data may lose it's
     * deterministic properties. This is caused by the {@link DataFactory} approach to random values
     * generation. The {@link java.util.Random} instance is provided as a static field, therefore
     * making it impossible to choose a different instance. As a workaround, this method injects
     * given {@link DeterministicRandom} into that static field using reflection, effectively
     * overwriting it. If multiple such injections were to be done during a parallel tests
     * execution, the data would no longer be deterministic (race condition).
     */
    private static DataFactory createDataFactory(final DeterministicRandom deterministicRandom) {
        try {
            LOG.fine("setting global random field of " + DataFactory.class.getName());
            final Field field = DataFactory.class.getDeclaredField("random");
            field.setAccessible(true);
            field.set(null, deterministicRandom);
            LOG.fine(
                "global random field of " + DataFactory.class.getName()
                    + " set correctly, returning"
            );
            return new DataFactory();
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new GlobalDataFactoryInstanceInitializationException(exception);
        }
    }

}
