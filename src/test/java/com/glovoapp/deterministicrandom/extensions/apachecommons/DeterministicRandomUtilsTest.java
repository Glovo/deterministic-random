package com.glovoapp.deterministicrandom.extensions.apachecommons;

import static com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.methodArgument;

import com.devskiller.jfairy.Fairy;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.MethodUnderTest;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.TypedArguments;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class DeterministicRandomUtilsTest {

    private static final DeterministicRandomTestCases<RandomUtils> TEST_CASES
        = new DeterministicRandomTestCases<>();

    @BeforeAll
    static void makeDeterministic() {
        DeterministicRandomUtils.makeDeterministic();
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<RandomUtils> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<RandomUtils> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    /**
     * This test case has been deliberately excluded from this test as {@link RandomUtils} is a
     * static methods collection and the {@link com.glovoapp.deterministicrandom.DeterministicRandom}
     * object can only be injected once.
     */
    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    @Disabled("testing not possible due to global state limitations")
    void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(
        final MethodUnderTest<RandomUtils> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<RandomUtils> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<? extends Arguments> provideMethodReferences() {
        return Stream.<TypedArguments<Fairy>>of(
            methodArgument("nextInt()", RandomUtils::nextInt),
            methodArgument("nextBoolean()", RandomUtils::nextBoolean),
            methodArgument("nextDouble()", RandomUtils::nextDouble),
            methodArgument("nextFloat()", RandomUtils::nextFloat)
        );
    }

}