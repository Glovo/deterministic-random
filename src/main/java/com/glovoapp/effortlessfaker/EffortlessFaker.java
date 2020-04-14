package com.glovoapp.effortlessfaker;

import javax.annotation.Nonnull;

/**
 * Allows easy creation of fake objects. The {@link #fillIn(Class)} method should be the only entry-point to this API.
 * Compatible classes must:
 * <p><ul>
 * <li>not inherit from any other class
 * <li>have constructor (of any visibility) that allows creating them
 * <li>are composed of primitive types or other data classes
 * </ul><p>
 * Each {@link EffortlessFaker} implementation should strive to:
 * <p><ul>
 * <li>require at most single line of code for initializing any data class
 * <li>be easily configurable (achieved via {@link DataProvider} interface)
 * <li>be as type-safe as possible
 * </ul><p>
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
