package com.glovoapp.effortlessfaker.reflection;

import static java.util.stream.IntStream.range;

import com.glovoapp.effortlessfaker.DataProvider;
import java.util.stream.Collector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CollectibleDataProvider<T, Collectible> implements DataProvider<Collectible> {

    private static final int DEFAULT_MAX_SIZE = 10;

    private final DataProvider<T> delegate;
    private final int maximumSize;
    private final Collector<T, ?, Collectible> collector;

    public CollectibleDataProvider(final DataProvider<T> delegate,
                                   final Collector<T, ?, Collectible> collector) {
        this(delegate, DEFAULT_MAX_SIZE, collector);
    }

    @Override
    public final Collectible provideData() {
        return range(0, maximumSize).mapToObj(index -> delegate.provideData())
                                    .collect(collector);
    }

}
