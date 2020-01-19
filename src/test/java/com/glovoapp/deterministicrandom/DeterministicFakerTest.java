package com.glovoapp.deterministicrandom;

import static com.glovoapp.deterministicrandom.DeterministicRandomTestCases.methodArgument;

import java.util.stream.Stream;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases.MethodUnderTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

// TODO: figure out why this test fails, some day
@Disabled("for some magical reason these tests do not work with Faker")
class DeterministicFakerTest {

    private static final DeterministicRandomTestCases<DeterministicFaker> TEST_CASES
        = new DeterministicRandomTestCases<>(DeterministicFaker::new);

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(final MethodUnderTest<DeterministicFaker> method) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(final MethodUnderTest<DeterministicFaker> method) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(final MethodUnderTest<DeterministicFaker> method) {
        TEST_CASES.shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(final MethodUnderTest<DeterministicFaker> method) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<Arguments> provideMethodReferences() {
        return Stream.of(
            methodArgument("name().name()", (DeterministicFaker df) -> df.name().name()),
            methodArgument("name().firstName()", (DeterministicFaker df) -> df.name().firstName()),
            methodArgument("name().lastName()", (DeterministicFaker df) -> df.name().lastName()),
            methodArgument("animal().name()", (DeterministicFaker df) -> df.animal().name())
        );
    }

}