package jezorko.com.github.random;

public final class RandomNumberGeneratorInitializationException extends RuntimeException {

    RandomNumberGeneratorInitializationException(final Throwable cause) {
        super("failed to create an instance of RNG", cause);
    }

}
