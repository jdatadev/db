package dev.jdata.db.utils.adt.byindex;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjIntFunction;
import dev.jdata.db.utils.scalars.Integers;

public interface IIntByIndexView extends IByIndexView, IIntByIndexGetters {

    @Override
    default void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    default void toHexString(long index, StringBuilder sb) {

        Integers.toHexUnsigned(get(index), sb);
    }

    default <P> int[] toArray(int numElements, P parameter, ObjIntFunction<P, int[]> createArray) {

        Checks.isIntNumElements(numElements);
        Objects.requireNonNull(createArray);

        final int[] dstArray = createArray.apply(parameter, numElements);

        toArray(dstArray, numElements);

        return dstArray;
    }

    default void toArray(int[] dst, int numElements) {

        Objects.requireNonNull(dst);
        Checks.isIntNumElements(numElements);

        for (int i = 0; i < numElements; ++ i) {

            dst[i] = get(i);
        }
    }
}
