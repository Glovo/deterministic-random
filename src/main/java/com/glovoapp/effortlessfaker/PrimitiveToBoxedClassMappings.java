package com.glovoapp.effortlessfaker;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

import java.util.Optional;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter(PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
enum PrimitiveToBoxedClassMappings {

    BOOLEAN_PRIMITIVE_TO_BOXED(boolean.class, Boolean.class),
    BYTE_PRIMITIVE_TO_BOXED(byte.class, Byte.class),
    CHAR_PRIMITIVE_TO_BOXED(char.class, Character.class),
    SHORT_PRIMITIVE_TO_BOXED(short.class, Short.class),
    INT_PRIMITIVE_TO_BOXED(int.class, Integer.class),
    LONG_PRIMITIVE_TO_BOXED(long.class, Long.class),
    FLOAT_PRIMITIVE_TO_BOXED(float.class, Float.class),
    DOUBLE_PRIMITIVE_TO_BOXED(double.class, Double.class),

    BOOLEAN_BOXED_TO_PRIMITIVE(Boolean.class, boolean.class),
    BYTE_BOXED_TO_PRIMITIVE(Byte.class, byte.class),
    CHAR_BOXED_TO_PRIMITIVE(Character.class, char.class),
    SHORT_BOXED_TO_PRIMITIVE(Short.class, short.class),
    INT_BOXED_TO_PRIMITIVE(Integer.class, int.class),
    LONG_BOXED_TO_PRIMITIVE(Long.class, long.class),
    FLOAT_BOXED_TO_PRIMITIVE(Float.class, float.class),
    DOUBLE_BOXED_TO_PRIMITIVE(Double.class, double.class);

    private final Class<?> mapFrom;
    private final Class<?> mapTo;

    static Optional<Class<?>> getCompatibleClass(@Nonnull final Class<?> boxedOrPrimitive) {
        requireNonNull(boxedOrPrimitive);
        final Optional<? extends Class<?>> compatibleClass = stream(values())
            .filter(mapping -> mapping.mapFrom.equals(boxedOrPrimitive))
            .map(PrimitiveToBoxedClassMappings::getMapTo)
            .findAny();

        if (!compatibleClass.isPresent() && boxedOrPrimitive.isPrimitive()) {
            throw new MissingPrimitiveToBoxedMappingException(boxedOrPrimitive);
        } else {
            @SuppressWarnings("unchecked") final Optional<Class<?>> result = (Optional<Class<?>>) compatibleClass;
            return result;
        }
    }

}
