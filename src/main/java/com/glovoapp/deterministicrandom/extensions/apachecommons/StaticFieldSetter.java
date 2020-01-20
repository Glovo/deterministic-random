package com.glovoapp.deterministicrandom.extensions.apachecommons;

import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final class StaticFieldSetter {

    static void makeDeterministic(final Class<?> randomUtilsClass,
                                  final String fieldName,
                                  final DeterministicRandom deterministicRandom) {
        try {
            final Field randomGeneratorField = randomUtilsClass.getDeclaredField(fieldName);
            randomGeneratorField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(
                randomGeneratorField,
                randomGeneratorField.getModifiers() & ~Modifier.FINAL
            );

            randomGeneratorField.set(null, deterministicRandom);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new StaticUtilsClassDeterministicRandomInitializationException(
                randomUtilsClass,
                exception
            );
        }
    }

}
