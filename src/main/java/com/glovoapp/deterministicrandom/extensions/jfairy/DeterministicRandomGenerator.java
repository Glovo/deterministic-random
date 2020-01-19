package com.glovoapp.deterministicrandom.extensions.jfairy;

import com.glovoapp.deterministicrandom.DeterministicRandom;
import java.util.Arrays;
import org.apache.commons.math3.random.RandomGenerator;

final class DeterministicRandomGenerator implements RandomGenerator {

    private final DeterministicRandom delegate;

    DeterministicRandomGenerator(final DeterministicRandom deterministicRandom) {
        this.delegate = deterministicRandom;
    }

    @Override
    public final void setSeed(final int seed) {
        delegate.setSeed(seed);
    }

    @Override
    public final void setSeed(final int[] seed) {
        delegate.setSeed(Arrays.hashCode(seed));
    }

    @Override
    public final void setSeed(final long seed) {
        delegate.setSeed(seed);
    }

    @Override
    public final void nextBytes(final byte[] bytes) {
        delegate.nextBytes(bytes);
    }

    @Override
    public final int nextInt() {
        return delegate.nextInt();
    }

    @Override
    public final int nextInt(final int bound) {
        return delegate.nextInt(bound);
    }

    @Override
    public final long nextLong() {
        return delegate.nextLong();
    }

    @Override
    public final boolean nextBoolean() {
        return delegate.nextBoolean();
    }

    @Override
    public final float nextFloat() {
        return delegate.nextFloat();
    }

    @Override
    public final double nextDouble() {
        return delegate.nextDouble();
    }

    @Override
    public final double nextGaussian() {
        return delegate.nextGaussian();
    }

}
