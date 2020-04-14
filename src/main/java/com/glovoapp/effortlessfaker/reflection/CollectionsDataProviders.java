package com.glovoapp.effortlessfaker.reflection;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.glovoapp.effortlessfaker.DataProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class CollectionsDataProviders {

    private static final AtomicBoolean HAS_MAP_KEY_CONFLICT_OCCURRED_YET = new AtomicBoolean(false);

    public static <T> DataProvider<List<T>> ofList(final DataProvider<T> delegate) {
        return new CollectibleDataProvider<>(delegate, toList());
    }

    public static <T> DataProvider<Set<T>> ofSet(final DataProvider<T> delegate) {
        return new CollectibleDataProvider<>(delegate, toSet());
    }

    public static <K, V> DataProvider<Map<K, V>> ofMap(final DataProvider<K> keyProvider,
                                                       final DataProvider<V> valueProvider) {
        return new CollectibleDataProvider<>(
            () -> new MapEntry<>(keyProvider.provideData(), valueProvider.provideData()),
            toMap(MapEntry::getKey, MapEntry::getValue, (firstValue, secondValue) -> {
                if (!HAS_MAP_KEY_CONFLICT_OCCURRED_YET.getAndSet(true)) {
                    final Logger log = Logger.getLogger(CollectionsDataProviders.class.getSimpleName());
                    log.warning(
                        "duplicate key occurred when generating a new Map instance; "
                            + "this is usually not a big deal (duplicate will be ignored) "
                            + "but you might want to consider using a delegate data provider "
                            + "with greater number of possible values and/or higher entropy"
                    );
                }
                return firstValue;
            })
        );
    }

    @Getter
    @RequiredArgsConstructor
    private static class MapEntry<K, V> {

        private final K key;
        private final V value;

    }

}
