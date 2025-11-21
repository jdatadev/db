package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

class BaseElementsHelper {

    static final class ToArrayParameter<T> {

        final T result;

        int index;

        ToArrayParameter(int numElements, IntFunction<T> createArray) {

            Checks.isIntNumElements(numElements);
            Objects.requireNonNull(createArray);

            this.result = createArray.apply(numElements);

            this.index = 0;
        }
    }
}
