package com.glovoapp.deterministicrandom;

import static com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.methodArgument;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.MethodUnderTest;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.TypedArguments;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DeterministicRandomTest {

    private static final DeterministicRandomTestCases<DeterministicRandom> TEST_CASES
        = new DeterministicRandomTestCases<>(DeterministicRandom::new);

    @Test
    void nextInt_shouldThrow_whenBoundIsZero() {
        final DeterministicRandom deterministicRandom = new DeterministicRandom();

        assertThrows(Exception.class, () -> deterministicRandom.nextInt(0));
    }

    @Test
    void nextInt_shouldThrow_whenBoundIsNegative() {
        final DeterministicRandom deterministicRandom = new DeterministicRandom();

        assertThrows(Exception.class, () -> deterministicRandom.nextInt(-10));
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<DeterministicRandom> method) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<DeterministicRandom> method) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(
        final MethodUnderTest<DeterministicRandom> method) {
        TEST_CASES.shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<DeterministicRandom> method) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<? extends Arguments> provideMethodReferences() {
        return Stream.<TypedArguments<DeterministicRandom>>of(
            methodArgument("nextBoolean()", DeterministicRandom::nextBoolean),
            methodArgument("nextInt()", DeterministicRandom::nextInt),
            methodArgument("nextInt(bound)", (DeterministicRandom dr) -> dr.nextInt(100)),
            methodArgument("nextLong()", DeterministicRandom::nextLong),
            methodArgument("nextFloat()", DeterministicRandom::nextFloat),
            methodArgument("nextDouble()", DeterministicRandom::nextDouble)
        );
    }

}