package jezorko.com.github.random;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.SeedException;

/**
 * <h1 color="red">
 * This class is unsafe to use in production environment.<br/> Use this class for anything but
 * testing at your own risk!
 * </h1>
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
 */
class DeterministicRandom extends Random {

    private final Map<Integer, ThreadLocal<Random>> stackTraceHashCodesToRandoms = new ConcurrentHashMap<>();
    private final Function<? super Integer, ? extends Random> randomOfSeedSupplier;

    DeterministicRandom() {
        this(it -> {
            try {
                return new MersenneTwisterRNG(new DeterministicLongSeedGenerator(it));
            } catch (SeedException exception) {
                throw new RandomNumberGeneratorInitializationException(exception);
            }
        });
    }

    private DeterministicRandom(final Function<? super Integer, ? extends Random> randomOfSeedSupplier) {
        this.randomOfSeedSupplier = randomOfSeedSupplier;
    }

    @Override
    public synchronized void setSeed(long seed) {
        // ignore
    }

    @Override
    protected int next(int bits) {
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
    public void nextBytes(byte[] bytes) {
        getForCurrentStackTrace().nextBytes(bytes);
    }

    @Override
    public int nextInt() {
        return getForCurrentStackTrace().nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return getForCurrentStackTrace().nextInt(bound);
    }

    @Override
    public long nextLong() {
        return getForCurrentStackTrace().nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return getForCurrentStackTrace().nextBoolean();
    }

    @Override
    public float nextFloat() {
        return getForCurrentStackTrace().nextFloat();
    }

    @Override
    public double nextDouble() {
        return getForCurrentStackTrace().nextDouble();
    }

    @Override
    public synchronized double nextGaussian() {
        return getForCurrentStackTrace().nextGaussian();
    }

    @Override
    public IntStream ints() {
        return getForCurrentStackTrace().ints();
    }

    @Override
    public IntStream ints(final long streamSize) {
        return getForCurrentStackTrace().ints(streamSize);
    }

    @Override
    public IntStream ints(final int randomNumberOrigin,
                          final int randomNumberBound) {
        return getForCurrentStackTrace().ints(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public IntStream ints(final long streamSize,
                          final int randomNumberOrigin,
                          final int randomNumberBound) {
        return getForCurrentStackTrace().ints(streamSize, randomNumberOrigin, randomNumberBound);
    }


    @Override
    public LongStream longs() {
        return getForCurrentStackTrace().longs();
    }

    @Override
    public LongStream longs(final long streamSize) {
        return getForCurrentStackTrace().longs(streamSize);
    }

    @Override
    public LongStream longs(final long randomNumberOrigin,
                            final long randomNumberBound) {
        return getForCurrentStackTrace().longs(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public LongStream longs(final long streamSize,
                            final long randomNumberOrigin,
                            final long randomNumberBound) {
        return getForCurrentStackTrace().longs(streamSize, randomNumberOrigin, randomNumberBound);
    }

    @Override
    public DoubleStream doubles() {
        return getForCurrentStackTrace().doubles();
    }

    @Override
    public DoubleStream doubles(final long streamSize) {
        return getForCurrentStackTrace().doubles(streamSize);
    }

    @Override
    public DoubleStream doubles(final double randomNumberOrigin,
                                final double randomNumberBound) {
        return getForCurrentStackTrace().doubles(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public DoubleStream doubles(final long streamSize,
                                final double randomNumberOrigin,
                                final double randomNumberBound) {
        return getForCurrentStackTrace().doubles(streamSize, randomNumberOrigin, randomNumberBound);
    }

    private Random getForCurrentStackTrace() {
        return stream(currentThread().getStackTrace())
            .map(StackTraceElement::hashCode)
            .reduce(Objects::hash)
            .map(hashCode -> stackTraceHashCodesToRandoms.computeIfAbsent(hashCode, code ->
                ThreadLocal.<Random>withInitial(() -> randomOfSeedSupplier.apply(code))
            ))
            .map(ThreadLocal::get)
            .orElseThrow(InternalException::new);
    }

    @Override
    public String toString() {
        return DeterministicRandom.class.getSimpleName()
            + "(called from "
            + stackTraceHashCodesToRandoms.size()
            + " places)";
    }

}
