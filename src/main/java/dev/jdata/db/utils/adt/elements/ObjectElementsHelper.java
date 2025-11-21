package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.byindex.IObjectByIndexView;
import dev.jdata.db.utils.checks.Checks;

class ObjectElementsHelper extends BaseElementsHelper {

    static <T> T[] toArrayIterable(IObjectIterable<T> objectIterable, int arrayLength, IntFunction<T[]> createArray) {

        Objects.requireNonNull(objectIterable);
        Checks.isArrayLength(arrayLength);
        Objects.requireNonNull(createArray);

        final ToArrayParameter<T[]> toArrayParameter = new ToArrayParameter<>(arrayLength, createArray);

        objectIterable.forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }

    static <T> T[] toArrayIndexView(IObjectByIndexView<T> objectByIndex, int arrayLength, IntFunction<T[]> createArray) {

        Objects.requireNonNull(objectByIndex);
        Checks.isArrayLength(arrayLength);
        Objects.requireNonNull(createArray);

        final T[] dst = createArray.apply(arrayLength);

        objectByIndex.toArray(dst, arrayLength);

        return dst;
    }
}
