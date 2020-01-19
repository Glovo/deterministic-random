package com.glovoapp.deterministicrandom.extensions.jfairy;

import com.devskiller.jfairy.producer.RandomGenerator;
import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.random.RandomDataGenerator;

/**
 * Copy-and-paste from {@link RandomGenerator} but with {@link RandomDataGenerator}.
 */
final class DeterministicJFairyRandomGenerator extends RandomGenerator {

    private final RandomDataGenerator randomDataGenerator;

    DeterministicJFairyRandomGenerator(final DeterministicRandom deterministicRandom) {
        randomDataGenerator = new RandomDataGenerator(
            new DeterministicRandomGenerator(deterministicRandom)
        );
    }

    @Override
    public final boolean nextBoolean() {
        return randomDataGenerator.getRandomGenerator()
                                  .nextBoolean();
    }

    @Override
    public final <T> List<T> shuffle(final List<T> elements) {
        Collections.shuffle(elements, (Random) randomDataGenerator.getRandomGenerator());
        return elements;
    }

    @Override
    public final int nextInt(final int min, final int max) {
        if (min == max) {
            return min;
        }
        return randomDataGenerator.nextInt(min, max);
    }

    @Override
    public final long nextDouble(final long min, final long max) {
        return randomDataGenerator.nextLong(min, max);
    }

    @Override
    public final double nextDouble(final double min, final double max) {
        return randomDataGenerator.nextUniform(min, max);
    }

}
