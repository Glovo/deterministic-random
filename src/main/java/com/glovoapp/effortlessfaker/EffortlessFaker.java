package com.glovoapp.effortlessfaker;

import javax.annotation.Nonnull;

/**
 * Allows easy creation of fake objects. The {@link #fillIn(Class)} method should be the only entry-point to this API.
 *
 * @see EffortlessFakerBuilder
 */
public interface EffortlessFaker {

    /**
     * Creates a new object of given type, initialized with random data.
     *
     * @param classToFill    the class of object that will be initialized
     * @param <ObjectToFill> type of the object to be initialized
     * @return initialized object of given type, never null
     */
    @Nonnull
    <ObjectToFill> ObjectToFill fillIn(@Nonnull final Class<? extends ObjectToFill> classToFill);

}
