package dev.jdata.db.utils.adt.collections;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

public class CollectionUtil {

    @FunctionalInterface
    public interface CollectionElementGetter<T, C> {

        T getAt(C collection, int index);
    }

    public static <T, C> T[] toArray(C collection, int numElements, IntFunction<T[]> createArray, CollectionElementGetter<T, C> elementGetter) {

        Objects.requireNonNull(collection);
        Checks.isIntNumElements(numElements);
        Objects.requireNonNull(elementGetter);

        final T[] result = createArray.apply(numElements);

        for (int i = 0; i < numElements; ++ i) {

            result[i] = elementGetter.getAt(collection, i);
        }

        return result;
    }
}
