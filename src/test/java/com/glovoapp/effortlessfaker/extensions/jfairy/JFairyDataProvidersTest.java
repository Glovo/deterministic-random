package com.glovoapp.effortlessfaker.extensions.jfairy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.glovoapp.deterministicrandom.extensions.jfairy.DeterministicFairy;
import com.glovoapp.effortlessfaker.DataProvider;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JFairyDataProvidersTest {

    @ParameterizedTest
    @MethodSource("getValidFieldNamesAndTypes")
    void getProviderFor_shouldReturnValidProviders_givenValidField(final String fieldName, final Class<?> fieldType) {
        final JFairyDataProviders providers = JFairyDataProviders.create(DeterministicFairy.create());

        final Optional<? extends DataProvider<?>> provider = providers.getProviderFor(fieldName, fieldType);

        assertTrue(provider.isPresent());
        assertNotNull(provider.get()
                              .provideData());
    }

    static Stream<Arguments> getValidFieldNamesAndTypes() {
        return Stream.of(
            arguments("firstName", String.class),
            arguments("firstName", CharSequence.class),
            arguments("first_name", String.class),
            arguments("f_I_r_S_t_N_a_M_e", String.class),
            arguments("any integer", Integer.class)
        );
    }


}