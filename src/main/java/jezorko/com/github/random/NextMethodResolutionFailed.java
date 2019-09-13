package jezorko.com.github.random;

public final class NextMethodResolutionFailed extends RuntimeException {

    NextMethodResolutionFailed(final Throwable cause) {
        super("failed to resolve and call Random#next(int) method", cause);
    }

}
