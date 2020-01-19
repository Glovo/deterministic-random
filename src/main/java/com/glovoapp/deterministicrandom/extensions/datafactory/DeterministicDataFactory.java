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
 * DeterministicRandom}.
 */
public final class DeterministicDataFactory {

    private static final Logger LOG = Logger.getLogger(DeterministicDataFactory.class.getName());
    private static final AtomicReference<DataFactory> DATA_FACTORY = new AtomicReference<>(null);

    public static DataFactory create() {
        return create(new DeterministicRandom());
    }

    public static DataFactory create(final DeterministicRandom deterministicRandom) {
        return DATA_FACTORY.updateAndGet(currentFactory -> {
            if (currentFactory != null) {
                throw new GlobalDataFactoryInstanceAlreadyExistsException();
            } else {
                return createDataFactory(deterministicRandom);
            }
        });
    }

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
