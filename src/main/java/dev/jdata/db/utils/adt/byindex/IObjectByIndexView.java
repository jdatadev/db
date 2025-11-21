package dev.jdata.db.utils.adt.byindex;

import java.util.Objects;
import java.util.function.BiPredicate;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjIntFunction;

public interface IObjectByIndexView<T> extends IByIndexView, IObjectByIndexGetters<T> {

    @Override
    default void toString(long index, StringBuilder sb) {

        sb.append(Objects.toString(get(index)));
    }

    @Override
    default void toHexString(long index, StringBuilder sb) {

        toString(index, sb);
    }

    default <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        Checks.isLongIndex(startIndex);
        Checks.isLongNumElements(numElements);

        boolean found = false;

        final long endIndex = startIndex + numElements;

        for (long i = startIndex; i < endIndex; ++ i) {

            if (predicate.test(get(i), parameter)) {

                found = true;
                break;
            }
        }

        return found;
    }

    default <P> T[] toArray(int numElements, P parameter, ObjIntFunction<P, T[]> createArray) {

        Checks.isIntNumElements(numElements);
        Objects.requireNonNull(createArray);

        final T[] dstArray = createArray.apply(parameter, numElements);

        toArray(dstArray, numElements);

        return dstArray;
    }

    default void toArray(T[] dst, int numElements) {

        Objects.requireNonNull(dst);
        Checks.isIntNumElements(numElements);

        for (int i = 0; i < numElements; ++ i) {

            dst[i] = get(i);
        }
    }
}
