package com.glovoapp.deterministicrandom.extensions.datafactory;

import static com.glovoapp.deterministicrandom.DeterministicRandomTestCases.methodArgument;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.glovoapp.deterministicrandom.DeterministicRandomTestCases;
import com.glovoapp.deterministicrandom.DeterministicRandomTestCases.MethodUnderTest;
import java.util.stream.Stream;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test case {@link DeterministicRandomTestCases#shouldReturnSameValues_whenMultipleRandomsCalledFromSameLine(MethodUnderTest)}
 * has been deliberately excluded from this test as only one, global instance of the deterministic
 * {@link DataFactory} can exist at any given time.
 */
class DeterministicDataFactoryTest {

    private static final DataFactory FACTORY = DeterministicDataFactory.create();

    private static final DeterministicRandomTestCases<DataFactory> TEST_CASES
        = new DeterministicRandomTestCases<>(() -> FACTORY);

    @Test
    void shouldCreateTheFactory_whenStaticMethodIsCalledOnce() {
        assertNotNull(FACTORY);
    }

    @Test
    void shouldFailToCreateTheFactory_whenGlobalInstanceAlreadyExists() {
        assertThrows(
            GlobalDataFactoryInstanceAlreadyExistsException.class,
            DeterministicDataFactory::create
        );
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledFromMultipleLines(
        final MethodUnderTest<DataFactory> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledFromMultipleLines(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnDifferentValues_whenCalledInALoop(
        final MethodUnderTest<DataFactory> method
    ) {
        TEST_CASES.shouldReturnDifferentValues_whenCalledInALoop(method);
    }

    @ParameterizedTest
    @MethodSource("provideMethodReferences")
    void shouldReturnSameValues_whenCalledFromDifferentThreads(
        final MethodUnderTest<DataFactory> method
    ) {
        TEST_CASES.shouldReturnSameValues_whenCalledFromDifferentThreads(method);
    }


    private static Stream<Arguments> provideMethodReferences() {
        return Stream.of(
            methodArgument("getName()", DataFactory::getName),
            methodArgument("getAddress()", DataFactory::getAddress),
            methodArgument("getCity()", DataFactory::getCity),
            methodArgument("getFirstName()", DataFactory::getFirstName),
            methodArgument("getLastName()", DataFactory::getLastName)
        );
    }

}