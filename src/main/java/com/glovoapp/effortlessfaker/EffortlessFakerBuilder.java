package com.glovoapp.effortlessfaker;

import static lombok.AccessLevel.PRIVATE;

import com.glovoapp.deterministicrandom.extensions.jfairy.DeterministicFairy;
import com.glovoapp.effortlessfaker.extensions.jfairy.JFairyDataProviders;
import com.glovoapp.effortlessfaker.reflection.ReflectionDataProviders;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.With;

@With
@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public final class EffortlessFakerBuilder {

    private final DataProviders dataProviders;

    static EffortlessFakerBuilder effortlessFaker() {
        return EffortlessFakerBuilder.builder()
                                     .dataProviders(
                                         ReflectionDataProviders.create(
                                             JFairyDataProviders.create(
                                                 DeterministicFairy.create()
                                             )
                                         )
                                     )
                                     .build();
    }

    final EffortlessFaker create() {
        return new EffortlessFaker() {
            @Override
            public <ObjectToFill> ObjectToFill fillIn(Class<? extends ObjectToFill> classToFill) {
                return dataProviders.getProviderFor("", classToFill)
                                    .orElseThrow(() -> new MissingDataProviderException(classToFill))
                                    .provideData();
            }
        };
    }

}
