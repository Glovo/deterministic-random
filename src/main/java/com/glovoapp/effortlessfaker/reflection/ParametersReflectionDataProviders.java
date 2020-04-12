package com.glovoapp.effortlessfaker.reflection;

import com.glovoapp.effortlessfaker.DataProvider;
import com.glovoapp.effortlessfaker.DataProviders;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ParametersReflectionDataProviders {

    static DataProvider<?> getFor(final Parameter parameter, final DataProviders delegate) {
        return getFor(parameter.getName(), parameter.getParameterizedType(), delegate);
    }

    static DataProvider<?> getFor(final String name, final Type type, final DataProviders delegate) {
        if (type instanceof Class) {
            final Class<?> typeAsClass = (Class<?>) type;
            return delegate.getProviderFor(name, typeAsClass)
                           .orElseGet(() -> {
                               try {
                                   return new ReflectionDataProvider<>(
                                       typeAsClass,
                                       delegate
                                   );
                               } catch (final Exception exception) {
                                   throw new DataClassParameterProviderInitializationException(
                                       name,
                                       typeAsClass,
                                       exception
                                   );
                               }
                           });
        } else if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            final Type rawType = parameterizedType.getRawType();

            if (rawType instanceof Class) {
                final Class<?> rawClass = (Class<?>) rawType;
                if (List.class.isAssignableFrom(rawClass)) {
                    final Type valueType = typeArguments[0];
                    return CollectionsDataProviders.ofList(getFor(name, valueType, delegate));
                } else if (Set.class.isAssignableFrom(rawClass)) {
                    final Type valueType = typeArguments[0];
                    return CollectionsDataProviders.ofSet(getFor(name, valueType, delegate));
                } else if (Map.class.isAssignableFrom(rawClass)) {
                    final Type keyType = typeArguments[0];
                    final Type valueType = typeArguments[1];
                    return CollectionsDataProviders.ofMap(
                        getFor(name, keyType, delegate),
                        getFor(name, valueType, delegate)
                    );
                } else {
                    throw new TypeNotSupportedException(name, type);
                }
            } else {
                throw new TypeNotSupportedException(name, type);
            }
        } else {
            throw new TypeNotSupportedException(name, type);
        }
    }

}
