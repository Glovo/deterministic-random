package com.glovoapp.deterministicrandom.extensions.faker;

import static com.glovoapp.deterministicrandom.DeterministicRandomTestCases.methodArgument;

import com.github.javafaker.Animal;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases.MethodUnderTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DeterministicFakerTest {

    private static final DeterministicRandomTestCases<Faker> TEST_CASES
        = new DeterministicRandomTestCases<>(DeterministicFaker::create);

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<Faker> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<Faker> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    // TODO: figure out why this test fails and fix it if possible
    //       or provide a better explanation for it to be disabled
    @Disabled("multiple instances of Faker are not deterministic, which breaks this test")
    void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(
        final MethodUnderTest<Faker> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    // TODO: figure out why this test fails and fix it if possible
    //       or provide a better explanation for it to be disabled
    @Disabled("multiple instances of Faker are not deterministic, which breaks this test")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<Faker> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<Arguments> provideMethodReferences() {
        return Stream.of(
            methodArgument("name().name()", Faker::name, Name::name),
            methodArgument("name().firstName()", Faker::name, Name::firstName),
            methodArgument("name().lastName()", Faker::name, Name::lastName),
            methodArgument("animal().name()", Faker::animal, Animal::name)
        );
    }

}