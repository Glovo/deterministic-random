package jezorko.com.github.random;

import org.uncommons.maths.random.SeedGenerator;

final class DeterministicLongSeedGenerator implements SeedGenerator {

    private final byte[] seed;

    DeterministicLongSeedGenerator(final long seed) {
        this.seed = String.valueOf(seed)
                          .getBytes();
    }

    @Override
    public byte[] generateSeed(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("seed length must be greater or equal to zero");
        }

        byte[] result = new byte[length];

        for (int i = 0; i < length; ++i) {
            result[i] = seed[i % seed.length];
        }

        return result;
    }

}
