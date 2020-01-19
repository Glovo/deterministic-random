package com.glovoapp.deterministicrandom;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public final class DeterministicRandomTestCases<T> {

    private final Supplier<T> randomsSupplier;

    public DeterministicRandomTestCases(Supplier<T> randomsSupplier) {
        this.randomsSupplier = randomsSupplier;
    }

    public interface MethodUnderTest<T> extends Function<T, Object> {

    }

    public final void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<T> method
    ) {
        final T deterministicRandom = randomsSupplier.get();

        final List<Object> results = new ArrayList<>();

        results.add(method.apply(deterministicRandom));
        results.add(method.apply(deterministicRandom));
        results.add(method.apply(deterministicRandom));
        results.add(method.apply(deterministicRandom));
        results.add(method.apply(deterministicRandom));
        results.add(method.apply(deterministicRandom));

        assertFalse(results.stream()
                           .allMatch(results.get(0)::equals));
    }

    public final void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<T> method
    ) {
        final T deterministicRandom = randomsSupplier.get();

        final List<Object> results = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            results.add(method.apply(deterministicRandom));
        }

        assertFalse(results.stream()
                           .allMatch(results.get(0)::equals));
    }

    public final void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(
        final MethodUnderTest<T> method
    ) {
        final List<T> randoms = range(0, 20)
            .mapToObj(it -> randomsSupplier.get())
            .collect(toList());

        final List<List<Object>> results = randoms.stream()
                                                  .map(deterministicRandom -> range(0, 50)
                                                      .mapToObj(it -> method.apply(
                                                          deterministicRandom
                                                      ))
                                                      .collect(toList()))
                                                  .collect(toList());

        final List<Object> firstResult = results.get(0);
        results.forEach(list -> assertIterableEquals(firstResult, list));
    }

    public final void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<T> method
    ) {
        final int randomsAmount = 50;

        final List<T> randoms = range(0, randomsAmount)
            .mapToObj(it -> randomsSupplier.get())
            .collect(toList());

        final ExecutorService executorService = Executors.newFixedThreadPool(randomsAmount);

        final List<Future<Object>> results = randoms.stream()
                                                    .map(random -> executorService.submit(() -> {
                                                        sleep(10L);
                                                        return (Object) range(0, 10)
                                                            .mapToObj(it -> method.apply(random))
                                                            .collect(toList());
                                                    }))
                                                    .collect(toList());

        assertTrue(results.stream()
                          .map(DeterministicRandomTestCases::getFromFuture)
                          .allMatch(getFromFuture(results.get(0))::equals));
    }

    private static <T> T getFromFuture(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static final class TypedArguments<T> implements Arguments {

        private final String name;
        private final MethodUnderTest<T> method;

        private TypedArguments(final String name,
                               final MethodUnderTest<T> method) {
            this.name = name;
            this.method = method;
        }

        @Override
        public Object[] get() {
            return new Object[]{
                new MethodUnderTest<T>() {
                    @Override
                    public Object apply(final T deterministicRandom) {
                        return method.apply(deterministicRandom);
                    }

                    @Override
                    public String toString() {
                        return name;
                    }
                }
            };
        }

    }

    public static <T, Field> TypedArguments<T> methodArgument(final String name,
                                                              final Function<T, Field> getter,
                                                              final MethodUnderTest<Field> method) {
        return methodArgument(name, (T it) -> method.apply(getter.apply(it)));
    }

    public static <T> TypedArguments<T> methodArgument(final String name,
                                                       final MethodUnderTest<T> method) {
        return new TypedArguments<>(name, method);
    }

}
