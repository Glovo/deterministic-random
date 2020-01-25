package com.glovoapp.deterministicrandom.extensions.apachecommons;

import static com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.methodArgument;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.devskiller.jfairy.Fairy;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.MethodUnderTest;
import com.glovoapp.deterministicrandom.test.DeterministicRandomTestCases.TypedArguments;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class DeterministicRandomStringUtilsTest {

    private static final DeterministicRandomTestCases<RandomStringUtils> TEST_CASES
        = new DeterministicRandomTestCases<>();

    @BeforeAll
    static void makeDeterministic() {
        DeterministicRandomStringUtils.makeDeterministic();
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<RandomStringUtils> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<RandomStringUtils> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    /**
     * This test case has been deliberately excluded from this test as {@link RandomStringUtils} is
     * a static methods collection and the {@link com.glovoapp.deterministicrandom.DeterministicRandom}
     * object can only be injected once.
     */
    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    @Disabled("testing not possible due to global state limitations")
    void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(
        final MethodUnderTest<RandomStringUtils> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<RandomStringUtils> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<? extends Arguments> provideMethodReferences() {
        return Stream.<TypedArguments<Fairy>>of(
            methodArgument("random(10)", () -> random(10)),
            methodArgument("randomAlphabetic(10)", () -> randomAlphabetic(10)),
            methodArgument("randomAlphanumeric(10)", () -> randomAlphanumeric(10))
        );
    }

}