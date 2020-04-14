package com.glovoapp.effortlessfaker;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PrimitiveToBoxedClassMappingsTest {

    private static final class NonPrimitiveOrBoxedClass {

    }

    @Test
    void getCompatibleClass_shouldReturnEmptyOptional_whenMappingOfNonPrimitiveClassIsMissing() {
        final Optional<Class<?>> actualResult = PrimitiveToBoxedClassMappings.getCompatibleClass(
            NonPrimitiveOrBoxedClass.class
        );

        assertFalse(actualResult.isPresent());
    }

}