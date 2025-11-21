package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.ILongByIndexView;
import dev.jdata.db.utils.checks.Checks;

class LongElementsHelper extends BaseElementsHelper {

    static long[] toArrayIterable(ILongIterable longIterable, int arrayLength) {

        Objects.requireNonNull(longIterable);
        Checks.isArrayLength(arrayLength);

        final ToArrayParameter<long[]> toArrayParameter = new ToArrayParameter<>(arrayLength, long[]::new);

        longIterable.forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }

    static long[] toArrayIndexView(ILongByIndexView longByIndex, int arrayLength) {

        Objects.requireNonNull(longByIndex);
        Checks.isArrayLength(arrayLength);

        final long[] dst = new long[arrayLength];

        longByIndex.toArray(dst, arrayLength);

        return dst;
    }
}
