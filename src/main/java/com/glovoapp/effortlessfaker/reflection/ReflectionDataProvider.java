package com.glovoapp.effortlessfaker.reflection;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import com.glovoapp.effortlessfaker.DataProvider;
import com.glovoapp.effortlessfaker.DataProviders;
import java.lang.reflect.Constructor;
import java.util.List;
import javax.annotation.Nonnull;

final class ReflectionDataProvider<AnyType> implements DataProvider<AnyType> {

    private final Class<?> type;
    private final DataProviders delegate;

    private ReflectionDataProvider(@Nonnull Class<?> type,
                                   @Nonnull DataProviders delegate) {
        this.type = requireDataClass(requireNonNull(type));
        this.delegate = requireNonNull(delegate);
    }

    static <AnyType> ReflectionDataProvider<AnyType> of(@Nonnull Class<? extends AnyType> type,
                                                        @Nonnull DataProviders delegate) {
        return new ReflectionDataProvider<>(type, delegate);
    }

    private Class<?> requireDataClass(final Class<?> dataClass) {
        if (!Object.class.equals(dataClass.getSuperclass())) {
            throw new DataClassNotInheritedFromObjectException(dataClass.getSuperclass());
        }
        return dataClass;
    }

    @Override

    public AnyType provideData() {
        final Constructor<?> constructor = stream(type.getDeclaredConstructors())
            .max(comparing(Constructor::getParameterCount))
            .orElseThrow(() -> new DataClassHasNoConstructorsException(type));

        final List<DataProvider<?>> parametersProviders = stream(constructor.getParameters())
            .map(parameter -> delegate.getProviderFor(parameter.getName(), parameter.getType())
                                      .orElseGet(
                                          () -> {
                                              try {
                                                  return new ReflectionDataProvider<>(
                                                      parameter.getType(),
                                                      delegate
                                                  );
                                              } catch (final Exception exception) {
                                                  throw new DataClassParameterProviderInitializationException(
                                                      parameter.getName(),
                                                      parameter.getType(),
                                                      exception
                                                  );
                                              }
                                          }
                                      )
            )
            .collect(toList());

        final Object[] parameters;
        try {
            parameters = parametersProviders.stream()
                                            .map(DataProvider::provideData)
                                            .toArray(Object[]::new);
        } catch (final Exception exception) {
            throw new DataClassParametersInitializationException(type, exception);
        }

        final Object data;
        try {
            constructor.setAccessible(true);
            data = constructor.newInstance(parameters);
        } catch (final Exception exception) {
            throw new DataClassConstructorException(exception);
        }

        try {
            @SuppressWarnings("unchecked") final AnyType dataAsRequestedType = (AnyType) data;
            return dataAsRequestedType;
        } catch (final Exception exception) {
            throw new DataClassCastingException(type, exception);
        }
    }

}
