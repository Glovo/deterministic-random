package com.glovoapp.deterministicrandom;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.glovoapp.deterministicrandom.DeterministicLongSeedGenerator;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DeterministicLongSeedGeneratorTest {

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -123, -1, 0, 1, 123, Long.MAX_VALUE})
    void generateSeed_shouldGenerateSeed_givenAnyLongValue(long initialSeed) {
        DeterministicLongSeedGenerator generator = new DeterministicLongSeedGenerator(initialSeed);

        assertDoesNotThrow(() -> generator.generateSeed(10));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -123, -1})
    void generateSeed_shouldFail_givenInvalidLength(int length) {
        DeterministicLongSeedGenerator generator = new DeterministicLongSeedGenerator(123L);
        assertThrows(IllegalArgumentException.class, () -> generator.generateSeed(length));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 123, 1024})
    void generateSeed_shouldGenerateSeed_givenAnyLengthGreaterOrEqualZero(int length) {
        DeterministicLongSeedGenerator generator = new DeterministicLongSeedGenerator(123L);

        byte[] result = assertDoesNotThrow(() -> generator.generateSeed(length));
        assertEquals(length, result.length);
    }

    @Test
    void generateSeed_shouldGenerateSameSeed_whenCalledMultipleTimes() {
        DeterministicLongSeedGenerator generator = new DeterministicLongSeedGenerator(0L);

        List<String> seeds = range(0, 100).mapToObj(it -> generator.generateSeed(123))
                                          .map(Base64.getEncoder()::encodeToString)
                                          .collect(toList());

        assertTrue(seeds.stream()
                        .allMatch(seeds.get(0)::equals));
    }

}