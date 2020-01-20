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