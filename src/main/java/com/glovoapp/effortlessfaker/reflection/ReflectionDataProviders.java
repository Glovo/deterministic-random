package com.glovoapp.effortlessfaker.reflection;

import com.glovoapp.effortlessfaker.DataProvider;
import com.glovoapp.effortlessfaker.DataProviders;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface ReflectionDataProviders extends DataProviders {

    static ReflectionDataProviders create(final DataProviders delegate) {
        return new ReflectionDataProviders() {
            @Nonnull
            @Override
            public <FieldType> Optional<DataProvider<FieldType>> getProviderFor(@Nonnull String fieldName,
                                                                                @Nonnull Class<? extends FieldType> fieldClass) {
                return Optional.of(
                    delegate.<FieldType>getProviderFor(fieldName, fieldClass)
                        .orElseGet(() -> ReflectionDataProvider.of(fieldClass, delegate))
                );
            }
        };
    }

}
