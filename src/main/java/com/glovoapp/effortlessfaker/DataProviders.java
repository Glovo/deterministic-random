package com.glovoapp.effortlessfaker;

import java.util.Optional;
import javax.annotation.Nonnull;

public interface DataProviders {

    @Nonnull
    <FieldType> Optional<DataProvider<FieldType>> getProviderFor(@Nonnull final String fieldName,
                                                                 @Nonnull final Class<? extends FieldType> fieldClass);

}
