package com.glovoapp.effortlessfaker.extensions.jfairy;

import static com.glovoapp.effortlessfaker.DataProvidersBuilder.dataProviders;

import com.devskiller.jfairy.Fairy;
import com.glovoapp.effortlessfaker.DataProviders;

public interface JFairyDataProviders extends DataProviders {

    static JFairyDataProviders create(final Fairy fairy) {
        final DataProviders delegate = dataProviders()
            .withDefaultPrimitiveProviders()
            .withGettersAsProviders(fairy.person())
            .withGettersAsProviders(fairy.company())
            .withGettersAsProviders(fairy.creditCard())
            // .withGettersAsProviders(fairy.iban()) // TODO: this is null for some reason, figure out why
            .withProvider(String.class, fairy.textProducer()::latinWord)
            .withProvider(Integer.class, () -> fairy.baseProducer()
                                                    .randomBetween(Integer.MIN_VALUE, Integer.MAX_VALUE))
            .withProvider(Long.class, () -> fairy.baseProducer()
                                                 .randomBetween(Long.MIN_VALUE, Long.MAX_VALUE))
            .withProvider(Character.class, () -> fairy.baseProducer()
                                                      .randomBetween(Character.MIN_VALUE, Character.MAX_VALUE))
            .withProvider(Boolean.class, fairy.baseProducer()::trueOrFalse)
            .build();

        return delegate::getProviderFor;
    }

}
