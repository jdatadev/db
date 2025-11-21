package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

class LongElementsHelper extends BaseElementsHelper {

    static long[] toArray(ILongIterable longIterable, int arrayLength) {

        Objects.requireNonNull(longIterable);
        Checks.isArrayLength(arrayLength);

        final ToArrayParameter<long[]> toArrayParameter = new ToArrayParameter<>(arrayLength, long[]::new);

        longIterable.forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }
}
