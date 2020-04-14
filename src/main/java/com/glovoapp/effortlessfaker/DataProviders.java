package com.glovoapp.effortlessfaker;

import java.util.Optional;

public interface DataProviders {

    static DataProviders empty() {
        return new DataProviders() {
            @Override
            public <FieldType> Optional<DataProvider<FieldType>> getProviderFor(String fieldName,
                                                                                Class<? extends FieldType> fieldClass) {
                return Optional.empty();
            }
        };
    }

    <FieldType> Optional<DataProvider<FieldType>> getProviderFor(final String fieldName,
                                                                 final Class<? extends FieldType> fieldClass);

}
