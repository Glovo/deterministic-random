package com.glovoapp.deterministicrandom;

import java.util.Random;

public interface RandomOfSeedConstructor {

    Random getBySeed(final int seed) throws Exception;

}
