package com.glovoapp.effortlessfaker.reflection;

import com.glovoapp.effortlessfaker.DataProvider;
import com.glovoapp.effortlessfaker.DataProviders;
import java.util.Optional;

public interface ReflectionDataProviders extends DataProviders {

    static ReflectionDataProviders create(final DataProviders delegate) {
        return new ReflectionDataProviders() {
            @Override
            public <FieldType> Optional<DataProvider<FieldType>> getProviderFor(String fieldName,
                                                                                Class<? extends FieldType> fieldClass) {
                return Optional.of(
                    delegate.<FieldType>getProviderFor(fieldName, fieldClass)
                        .orElseGet(() -> ReflectionDataProvider.of(fieldClass, delegate))
                );
            }
        };
    }

}
