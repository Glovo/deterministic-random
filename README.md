[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

## Overview

There are two approaches to providing data for testing.

First is to use random data (for example in [monkey testing](https://en.wikipedia.org/wiki/Monkey_testing)).
This allows us to generate big data sets with unique data points and therefore miss fewer corner cases.
Additionally, we save time because we don't need to come up with data values manually.

The second approach is to use static [test fixtures](https://en.wikipedia.org/wiki/Test_fixture).
These take a significantly longer time to create but benefit from being deterministic.
All consecutive test executions will yield the same results as the data does not change.

This library aims to combine the best of these two worlds: simplicity of using random data with deterministic nature of fixtures.

## Usage

Before using this library, please take time to understand how data affects your tests.

As a rule of thumb, this library is OK to use for tests where: 

 1. data does not matter, or
 2. data set is so large that manually creating it is not a viable option

If you need to test against data with some particular requirements (for example: a name with unicode characters), it might be better to specify it manually.

### DeterministicRandom class

The "deterministic random" data is provided by the `com.glovoapp.deterministicrandom.DeterministicRandom` class.
This class uses the current stack trace and a counter as a seed for generating values.

In consequence, the following program:

```java
public final class ExampleApplication {

    public static void main(final String... args) {
        final Random random = new DeterministicRandom();
        System.out.println("deterministic random value: " + random.nextLong());
    }

}
```

Will print the same message, no matter how many times it's executed.

#### Inheriting from `java.util.Random`

One could argue that the `DeterministicRandom` class is breaking [LSP](https://en.wikipedia.org/wiki/Liskov_substitution_principle) by inheriting from `java.util.Random`.
Being predictable as it is, the generated values are not "truly random".

This is however a philosophical rather than technical question.
Consider the following implementation:

```java
public final class ConstantValueReturningRandom extends java.util.Random {

    private final AtomicInteger counter = new AtomicInteger(Integer.MIN_VALUE);

    @Override
    public final int nextInt() {
        return counter.getAndIncrement();
    }

    // … other methods of java.util.Random implemented in similar fashion

}
```

Does the `nextInt` method return what is expected from it?
According to [the documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#nextInt--) the result must be:

 * pseudorandom
 * uniformly distributed; all possible int values are produced with (approximately) equal probability

It definitely meets the "uniform distribution" requirement.
Execute this method enough times and you will see that all possible `int` values are produced with exactly the same frequency.

The definition of "pseudorandom" is a bit fuzzy, though.
How well does a random number generator need to approximate truly random numbers?
This depends on the use case; obviously, `DeterministicRandom` is not meant to be of cryptographic quality. 

In conclusion, `DeterministicRandom` class *is* a valid random number generator… with an extremely low entropy.

### Extensions

There are a few static factories for popular random data generation libraries included in this project.
The libraries themselves are not included in the JAR and must be provided in the classpath.

Whenever a library accepts an instance of `java.util.Random`, an extension may be created for it.
The `DeterministicRandomTestCases` class can be used to assert compliance with `DeterministicRandom`'s behavior.

#### [jFairy](https://github.com/Devskiller/jfairy)

The deterministic extension for jFairy library is provided by the `com.glovoapp.deterministicrandom.extensions.jfairy.DeterministicFairy` static factory.
It supports setting locale and random generator.

```java
final Fairy fairy = DeterministicFairy.create();
```

#### [Java Faker](https://github.com/DiUS/java-faker)

The deterministic extension for Java Faker library is provided by the `com.glovoapp.deterministicrandom.extensions.faker.DeterministicFaker` static factory.
It allows setting locale and random generator.

```java
final Faker faker = DeterministicFaker.create();
```

For whatever reason, the Faker class itself is not deterministic.
It sometimes calls the underlying random generator more often that other times.
Because of this, the class may not be reliably tested with `DeterministicRandomTestCases`.

#### [DataFactory](https://github.com/andygibson/datafactory)

The deterministic extension for DataFactory library is provided by the `com.glovoapp.deterministicrandom.extensions.datafactory.DeterministicDataFactory` static factory.
It does not support locale.

```java
final DataFactory df = DeterministicDataFactory.create();
```

Due to the limitations of the `DataFactory` class, only one instance may be created with `DeterministicDataFactory`.
Please note this will modify the global state and alter the behavior of all `DataFactory` objects!

#### [Commons Lang](https://commons.apache.org/proper/commons-lang/)

The deterministic extension for Commons Lang 3 library is provided by the 
`com.glovoapp.deterministicrandom.extensions.apachecommons.DeterministicRandomUtils` and
`com.glovoapp.deterministicrandom.extensions.apachecommons.DeterministicRandomStringUtils`.
It does not support locale and modifies the global state of the `RandomUtils` and `RandomStringUtils` classes.

```java
DeterministicRandomStringUtils.makeDeterministic();

// all static method calls will now be deterministic
RandomStringUtils.randomAlphanumeric(10);
```

### EffortlessFaker class

The `EffortlessFaker` is a tool for populating new instances of data classes.

It may be created (and configured) using the builder:

```java
final EffortlessFaker faker = EffortlessFakerBuilder.effortlessFaker().create();
```

By default, the builder uses `DeterministicRandom` with `JFairy` extension to create the faker.

Then given a class like:

```java
final class SomeDataClass {

    private final String someStringParameter;
    private final int somePrimitiveParameter;

    // … constructors and getters

}
```

The faker will create a new instance and fill all the fields in:

```java
final SomeDataClass someDataObject = faker.fillIn(SomeDataClass.class);
someDataObject.getSomeStringParameter(); // returns "congue nibh proin"
someDataObject.getSomePrimitiveParameter(); // returns -212014988
```

Additionally, the faker uses names of fields to decide what values should be generated for them.
By default, names of fields matching methods of `JFairy` getters (such as `firstName`) are supported.

## Building the project

To compile and package the project as JAR file, run:

```bash
./gradlew :clean :test :fatJar
```

This will also run all the unit tests.