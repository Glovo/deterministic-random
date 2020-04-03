package com.glovoapp.effortlessfaker;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public final class DataProvidersBuilder {

    private static Predicate<String> defaultNamePredicateOf(final String fieldName) {
        return it -> it.replaceAll("_", "")
                       .equalsIgnoreCase(fieldName.replaceAll("_", ""));
    }

    private static <T> Predicate<Class<? extends T>> defaultClassPredicateOf(final Class<? extends T> fieldClass) {
        return it -> it.isAssignableFrom(fieldClass)
            || PrimitiveToBoxedClassMappings.getCompatibleClass(fieldClass)
                                            .filter(it::isAssignableFrom)
                                            .isPresent();
    }

    @With(PRIVATE)
    private final List<ProviderSpecification<?>> providers;

    public static DataProvidersBuilder dataProviders() {
        return DataProvidersBuilder.builder()
                                   .providers(emptyList())
                                   .build();
    }

    public final DataProvidersBuilder withDefaultPrimitiveProviders() {
        return withProvider(Void.class, () -> null).withProvider(Object.class, Object::new);
    }

    public final DataProvidersBuilder withGettersAsProviders(final Object gettersSource) {
        return withSpecifications(gettersAsProviders(gettersSource));
    }

    private List<ProviderSpecification<?>> gettersAsProviders(final Object gettersSource) {
        requireNonNull(gettersSource, "getters source must not be null, this is probably a configuration issue");
        return Optional.of(gettersSource)
                       .map(Object::getClass)
                       .map(Class::getMethods)
                       .map(Arrays::stream)
                       .orElseGet(Stream::empty)
                       .filter(method -> method.getName()
                                               .startsWith("get"))
                       .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                       .filter(method -> method.getParameterCount() == 0)
                       .map(method -> new ProviderSpecification<>(
                           defaultNamePredicateOf(method.getName()
                                                        .replace("get", "")),
                           defaultClassPredicateOf(method.getReturnType()),
                           () -> {
                               try {
                                   return method.invoke(gettersSource);
                               } catch (final Exception exception) {
                                   throw new DataProvidingException(exception);
                               }
                           }
                       ))
                       .collect(toList());
    }

    /**
     * Registers a catch-all provider for any fields of given type.
     */
    public final <FieldType> DataProvidersBuilder withProvider(final Class<? extends FieldType> fieldClass,
                                                               final DataProvider<FieldType> provider) {
        return withProvider(
            fieldName -> true,
            defaultClassPredicateOf(fieldClass),
            provider
        );
    }

    /**
     * Registers a catch-all provider for any fields whose type matches the predicate.
     */
    public final <FieldType> DataProvidersBuilder withProvider(
        final Predicate<Class<? extends FieldType>> fieldClassPredicate,
        final DataProvider<FieldType> provider
    ) {
        return withProvider(
            fieldName -> true,
            fieldClassPredicate,
            provider
        );
    }

    public final <FieldType> DataProvidersBuilder withProvider(final String fieldName,
                                                               final Class<? extends FieldType> fieldClass,
                                                               final DataProvider<FieldType> provider) {
        return withProvider(
            defaultNamePredicateOf(fieldName),
            defaultClassPredicateOf(fieldClass),
            provider
        );
    }

    public final <FieldType> DataProvidersBuilder withProvider(final String fieldName,
                                                               final Predicate<Class<? extends FieldType>> fieldClassPredicate,
                                                               final DataProvider<FieldType> provider) {
        return withProvider(
            defaultNamePredicateOf(fieldName),
            fieldClassPredicate,
            provider
        );
    }

    public final <FieldType> DataProvidersBuilder withProvider(final Predicate<String> fieldNamePredicate,
                                                               final Class<? extends FieldType> fieldClass,
                                                               final DataProvider<FieldType> provider) {
        return withProvider(
            fieldNamePredicate,
            defaultClassPredicateOf(fieldClass),
            provider
        );
    }

    public final <FieldType> DataProvidersBuilder withProvider(final Predicate<String> fieldNamePredicate,
                                                               final Predicate<Class<? extends FieldType>> fieldClassPredicate,
                                                               final DataProvider<FieldType> provider) {
        return withSpecifications(
            singleton(new ProviderSpecification<>(fieldNamePredicate, fieldClassPredicate, provider))
        );
    }

    private DataProvidersBuilder withSpecifications(final Collection<ProviderSpecification<?>> specifications) {
        return withProviders(
            concat(
                providers.stream(),
                specifications.stream()
            ).collect(toList())
        );
    }

    public final DataProviders build() {
        return new DataProviders() {
            @Nonnull
            @Override
            @SuppressWarnings("unchecked")
            public <FieldType> Optional<DataProvider<FieldType>> getProviderFor(@Nonnull String fieldName,
                                                                                @Nonnull Class<? extends FieldType> fieldClass) {
                return providers.stream()
                                .filter(specification -> specification.getFieldNamePredicate()
                                                                      .test(fieldName))
                                .filter(specification ->
                                    ((Predicate<Class<?>>) (specification.getFieldClassPredicate()))
                                        .test(fieldClass)
                                )
                                .map(ProviderSpecification::getProvider)
                                .findAny()
                                .map(provider -> (DataProvider<FieldType>) provider);
            }
        };
    }

    @With(PRIVATE)
    @Getter(PRIVATE)
    @RequiredArgsConstructor(access = PRIVATE)
    private static final class ProviderSpecification<FieldType> {

        private final Predicate<String> fieldNamePredicate;
        private final Predicate<Class<? extends FieldType>> fieldClassPredicate;
        private final DataProvider<FieldType> provider;

    }

}
