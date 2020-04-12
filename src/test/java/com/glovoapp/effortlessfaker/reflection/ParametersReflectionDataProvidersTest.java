package com.glovoapp.effortlessfaker.reflection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.glovoapp.effortlessfaker.DataProviders;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParametersReflectionDataProvidersTest {

    @Getter
    @RequiredArgsConstructor
    private static final class SimpleParameterizedType implements ParameterizedType {

        private final Type rawType;
        private final Type[] actualTypeArguments;
        private final Type ownerType;

    }

    @Getter
    @RequiredArgsConstructor
    private static final class NotAClassOrParameterizedType implements Type {

        @Override
        public final String getTypeName() {
            return "TEST";
        }

    }

    @SuppressWarnings("unused")
    private static final class UnsupportedGenericType<SomeGenericTypeArgument> {

    }


    @ParameterizedTest
    @MethodSource("getUnsupportedTypes")
    void getFor_shouldThrow_givenAnUnsupportedType(final Type unsupportedType) {
        assertThrows(TypeNotSupportedException.class, () ->
            ParametersReflectionDataProviders.getFor("", unsupportedType, DataProviders.empty())
        );
    }

    private static Stream<Arguments> getUnsupportedTypes() {
        return Stream.of(
            arguments(new SimpleParameterizedType(UnsupportedGenericType.class, null, null)),
            arguments(new SimpleParameterizedType(new SimpleParameterizedType(null, null, null), null, null)),
            arguments(new NotAClassOrParameterizedType())
        );
    }

}