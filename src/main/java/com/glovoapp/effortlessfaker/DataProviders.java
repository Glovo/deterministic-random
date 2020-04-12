package com.glovoapp.effortlessfaker;

import java.util.Optional;
import javax.annotation.Nonnull;

public interface DataProviders {

    static DataProviders empty() {
        return new DataProviders() {
            @Nonnull
            @Override
            public <FieldType> Optional<DataProvider<FieldType>> getProviderFor(@Nonnull String fieldName,
                                                                                @Nonnull Class<? extends FieldType> fieldClass) {
                return Optional.empty();
            }
        };
    }

    @Nonnull
    <FieldType> Optional<DataProvider<FieldType>> getProviderFor(@Nonnull final String fieldName,
                                                                 @Nonnull final Class<? extends FieldType> fieldClass);

}
