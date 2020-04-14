package com.glovoapp.effortlessfaker;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

class EffortlessFakerTest {

    @Getter
    @RequiredArgsConstructor
    static final class ExampleComposedDataClass {

        private final String exampleStringField;
        private final long examplePrimitiveField;

    }

    @Getter
    @RequiredArgsConstructor
    static final class ExampleDataClass {

        private final ExampleComposedDataClass exampleComplexField;
        private final Integer exampleWrapperField;

    }

    @Getter
    @RequiredArgsConstructor
    static final class ExampleDataClassWithCollection {

        private final List<ExampleComposedDataClass> exampleComplexFields;
        private final List<Set<Map<ExampleComposedDataClass, ExampleComposedDataClass>>> exampleComplicatedCollection;

    }

    static final class ExampleClassWithTwoConstructorWhereNoArgsOneThrows {

        ExampleClassWithTwoConstructorWhereNoArgsOneThrows() {
            throw new IllegalStateException("this constructor shouldn't be called!");
        }

        ExampleClassWithTwoConstructorWhereNoArgsOneThrows(final int firstField, final String secondField) {
        }

    }

    @Test
    void fillIn_shouldChooseConstructorThatHasMostParameters_givenDefaultFaker() {
        final EffortlessFaker effortlessFaker = EffortlessFakerBuilder.effortlessFaker()
                                                                      .create();

        assertDoesNotThrow(() -> effortlessFaker.fillIn(ExampleClassWithTwoConstructorWhereNoArgsOneThrows.class));
    }

    @Test
    void fillIn_shouldWorkOnClassFromReadme_givenDefaultFaker() {
        final EffortlessFaker effortlessFaker = EffortlessFakerBuilder.effortlessFaker()
                                                                      .create();

        final SomeDataClass exampleDataObject = effortlessFaker.fillIn(SomeDataClass.class);

        assertNotNull(exampleDataObject);
        assertNotNull(exampleDataObject.getSomeStringParameter());
    }

    @Test
    void fillIn_shouldReturnAProperDataClass_givenDefaultFaker() {
        final EffortlessFaker effortlessFaker = EffortlessFakerBuilder.effortlessFaker()
                                                                      .create();

        final ExampleDataClass exampleDataObject = effortlessFaker.fillIn(ExampleDataClass.class);
        final ExampleComposedDataClass complexField = exampleDataObject.getExampleComplexField();

        assertNotNull(exampleDataObject.getExampleWrapperField());
        assertNotNull(complexField);
        assertNotNull(complexField.getExampleStringField());
    }

    @Test
    void fillIn_shouldWorkForCollections_givenDefaultFaker() {
        final EffortlessFaker effortlessFaker = EffortlessFakerBuilder.effortlessFaker()
                                                                      .create();

        final ExampleDataClassWithCollection exampleDataObject = effortlessFaker.fillIn(
            ExampleDataClassWithCollection.class
        );

        assertNotNull(exampleDataObject.getExampleComplexFields());
        assertNotEquals(0, exampleDataObject.getExampleComplexFields()
                                            .size());
        assertNotNull(exampleDataObject.getExampleComplicatedCollection());
    }

    @Test
    void fillIn_shouldThrow_whenTheProviderIsMissing() {
        final EffortlessFaker effortlessFaker = EffortlessFakerBuilder.effortlessFaker()
                                                                      .withDataProviders(DataProviders.empty())
                                                                      .create();

        assertThrows(Exception.class, () -> effortlessFaker.fillIn(Object.class));
    }

}