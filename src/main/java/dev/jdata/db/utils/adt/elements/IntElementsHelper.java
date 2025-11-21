package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IIntByIndexView;
import dev.jdata.db.utils.checks.Checks;

class IntElementsHelper extends BaseElementsHelper {

    static int[] toArrayIterable(IIntIterable intIterable, int arrayLength) {

        Objects.requireNonNull(intIterable);
        Checks.isArrayLength(arrayLength);

        final ToArrayParameter<int[]> toArrayParameter = new ToArrayParameter<>(arrayLength, int[]::new);

        intIterable.forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }

    static int[] toArrayIndexView(IIntByIndexView intByIndex, int arrayLength) {

        Objects.requireNonNull(intByIndex);
        Checks.isArrayLength(arrayLength);

        final int[] dst = new int[arrayLength];

        intByIndex.toArray(dst, arrayLength);

        return dst;
    }
}
