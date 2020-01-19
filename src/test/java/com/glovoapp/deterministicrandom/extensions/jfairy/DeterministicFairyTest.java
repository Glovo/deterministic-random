package com.glovoapp.deterministicrandom.extensions.jfairy;

import static com.glovoapp.deterministicrandom.DeterministicRandomTestCases.methodArgument;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases.MethodUnderTest;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases.TypedArguments;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DeterministicFairyTest {

    private static final DeterministicRandomTestCases<Fairy> TEST_CASES
        = new DeterministicRandomTestCases<>(DeterministicFairy::create);

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<Fairy> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<Fairy> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(
        final MethodUnderTest<Fairy> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<Fairy> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<? extends Arguments> provideMethodReferences() {
        return Stream.<TypedArguments<Fairy>>of(
            methodArgument("person().getFullName()", Fairy::person, Person::getFullName),
            methodArgument("person().getFirstName()", Fairy::person, Person::getFirstName),
            methodArgument("person().getLastName()", Fairy::person, Person::getLastName)
        );
    }

}