package com.glovoapp.deterministicrandom;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.uncommons.maths.random.MersenneTwisterRNG;

/**
 * <h1 color="red"> This class is unsafe to use in production environment!</h1><br/>
 * <h2 color="red">Use this class for anything but testing at your own risk.</h2><br/>
 * Wrapper over {@link Random} whose execution is random, yet deterministic.<br/> <br/>
 * "Deterministic" in the context of this class means that given a position in code (call stack), it
 * will yield the same values for each execution of said code. This behavior includes code being
 * executed within different threads and processes.<br/> <br/>
 * <b>Example execution:</b>
 * <pre>
 *     final DeterministicRandom dr = new DeterministicRandom();
 *
 *     // binds result of execution to the position in code
 *     final int value = dr.nextInt(100);
 *
 *     // always prints the same result
 *     System.out.println(value);
 * </pre>
 * {@link DeterministicRandom} will keep track of previously generated values. This is to ensure
 * that collections created in loops will not be repetitive.<br/> <br/>
 * <b>Example loop execution:</b>
 * <pre>
 *     final DeterministicRandom dr = new DeterministicRandom();
 *
 *     final List<Integer> values = new ArrayList<>();
 *     for (int i = 0; i < 5; ++i) {
 *         // values generated in a loop are memorized
 *         values.add(dr.nextInt(100));
 *     }
 *
 *     // lists contains non-equal values yet they are the same each execution
 *     // example output: [80, 82, 34, 42, 85]
 *     System.out.println(values);
 * </pre>
 * <b>Testing</b><br/><br/>
 * For a complete specification of this class's behavior, please see the {@link
 * com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases}. All extensions should be
 * tested using this class.
 */
public final class DeterministicRandom extends Random {

    private final Map<Integer, ThreadLocal<Random>> stackTraceHashCodesToRandoms = new ConcurrentHashMap<>();
    private final RandomOfSeedConstructor randomOfSeedConstructor;

    /**
     * By default {@link MersenneTwisterRNG} will be used for generating random values.
     *
     * @see #DeterministicRandom(RandomOfSeedConstructor) constructor that allows custom generators
     */
    public DeterministicRandom() {
        this(it -> new MersenneTwisterRNG(new DeterministicLongSeedGenerator(it)));
    }

    /**
     * @param randomOfSeedConstructor to be used for creating random number generators
     */
    public DeterministicRandom(final RandomOfSeedConstructor randomOfSeedConstructor) {
        this.randomOfSeedConstructor = randomOfSeedConstructor;
    }

    @Override
    public final synchronized void setSeed(long seed) {
        // ignore
    }

    @Override
    protected final int next(int bits) {
        try {
            Random random = getForCurrentStackTrace();
            Method method = random.getClass()
                                  .getDeclaredMethod("next", Integer.class);
            method.setAccessible(true);
            return (Integer) method.invoke(random, bits);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new NextMethodResolutionFailed(exception);
        }
    }

    @Override
    public final void nextBytes(byte[] bytes) {
        getForCurrentStackTrace().nextBytes(bytes);
    }

    @Override
    public final int nextInt() {
        return getForCurrentStackTrace().nextInt();
    }

    @Override
    public final int nextInt(int bound) {
        return getForCurrentStackTrace().nextInt(bound);
    }

    @Override
    public final long nextLong() {
        return getForCurrentStackTrace().nextLong();
    }

    @Override
    public final boolean nextBoolean() {
        return getForCurrentStackTrace().nextBoolean();
    }

    @Override
    public final float nextFloat() {
        return getForCurrentStackTrace().nextFloat();
    }

    @Override
    public final double nextDouble() {
        return getForCurrentStackTrace().nextDouble();
    }

    @Override
    public final synchronized double nextGaussian() {
        return getForCurrentStackTrace().nextGaussian();
    }

    @Override
    public final IntStream ints() {
        return getForCurrentStackTrace().ints();
    }

    @Override
    public final IntStream ints(final long streamSize) {
        return getForCurrentStackTrace().ints(streamSize);
    }

    @Override
    public final IntStream ints(final int randomNumberOrigin,
                                final int randomNumberBound) {
        return getForCurrentStackTrace().ints(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final IntStream ints(final long streamSize,
                                final int randomNumberOrigin,
                                final int randomNumberBound) {
        return getForCurrentStackTrace().ints(streamSize, randomNumberOrigin, randomNumberBound);
    }


    @Override
    public final LongStream longs() {
        return getForCurrentStackTrace().longs();
    }

    @Override
    public final LongStream longs(final long streamSize) {
        return getForCurrentStackTrace().longs(streamSize);
    }

    @Override
    public final LongStream longs(final long randomNumberOrigin,
                                  final long randomNumberBound) {
        return getForCurrentStackTrace().longs(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final LongStream longs(final long streamSize,
                                  final long randomNumberOrigin,
                                  final long randomNumberBound) {
        return getForCurrentStackTrace().longs(streamSize, randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final DoubleStream doubles() {
        return getForCurrentStackTrace().doubles();
    }

    @Override
    public final DoubleStream doubles(final long streamSize) {
        return getForCurrentStackTrace().doubles(streamSize);
    }

    @Override
    public final DoubleStream doubles(final double randomNumberOrigin,
                                      final double randomNumberBound) {
        return getForCurrentStackTrace().doubles(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final DoubleStream doubles(final long streamSize,
                                      final double randomNumberOrigin,
                                      final double randomNumberBound) {
        return getForCurrentStackTrace().doubles(streamSize, randomNumberOrigin, randomNumberBound);
    }

    private Random getForCurrentStackTrace() {
        return stream(currentThread().getStackTrace())
            .map(StackTraceElement::hashCode)
            .reduce(Objects::hash)
            .map(hashCode -> stackTraceHashCodesToRandoms.computeIfAbsent(hashCode, code ->
                ThreadLocal.<Random>withInitial(() -> getRandomBySeedOrThrow(code))
            ))
            .map(ThreadLocal::get)
            .orElseThrow(InternalException::new);
    }

    private Random getRandomBySeedOrThrow(final int seed) {
        try {
            return randomOfSeedConstructor.getBySeed(seed);
        } catch (final Exception exception) {
            throw new RandomNumberGeneratorInitializationException(exception);
        }
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName()
            + "(called from "
            + stackTraceHashCodesToRandoms.size()
            + " places)";
    }

}
