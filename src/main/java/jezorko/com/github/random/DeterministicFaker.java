package jezorko.com.github.random;

import com.github.javafaker.Faker;
import java.util.Locale;

/**
 * <h1 color="red">
 * This class is unsafe to use in production environment.<br/>
 * Use this class for anything but testing at your own risk!
 * </h1>
 * A {@link Faker} that uses {@link DeterministicRandom} for generating values.
 */
public final class DeterministicFaker extends Faker {

    private static final DeterministicFaker FAKER = new DeterministicFaker();

    private final DeterministicRandom random;

    /**
     * @return a global instance of {@link DeterministicFaker}
     */
    public static Faker deterministic() {
        return FAKER;
    }

    public DeterministicFaker() {
        this(new DeterministicRandom());
    }

    private DeterministicFaker(final DeterministicRandom random) {
        super(random);
        this.random = random;
    }

    public DeterministicFaker(final Locale locale) {
        this(locale, new DeterministicRandom());
    }

    private DeterministicFaker(final Locale locale, final DeterministicRandom random) {
        super(locale, random);
        this.random = random;
    }

    @Override
    public String toString() {
        return DeterministicFaker.class.getSimpleName() + '(' + random + ')';
    }
}
