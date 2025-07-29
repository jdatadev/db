package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongIndexList extends BaseIntegerIndexList<long[]> implements ILongIndexListCommon {

    BaseLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType);

        Checks.isInitialCapacity(initialCapacity);

        this.elementsArray = new long[initialCapacity];
        this.numElements = 0;
    }

    BaseLongIndexList(AllocationType allocationType, long value) {
        super(allocationType, new long[] { value }, 1);
    }

    BaseLongIndexList(AllocationType allocationType, long[] values) {
        super(allocationType, values, values.length);
    }

    BaseLongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);

        Checks.checkNumElements(values, numElements);
    }

    BaseLongIndexList(BaseLongIndexList toCopy) {
        super(AllocationType.HEAP);

        final int toCopyNumElements = toCopy.numElements;

        this.elementsArray = Arrays.copyOf(toCopy.elementsArray, toCopyNumElements);
        this.numElements = toCopyNumElements;
    }

    final void initialize(long[] values, int numElements) {

        initialize(values, numElements, values.length);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            forEach.each(elementsArray[i], parameter);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            final R forEachResult = forEach.each(elementsArray[i], parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    public final long get(long index) {

        return elementsArray[Integers.checkUnsignedLongToUnsignedInt(index)];
    }

    @Override
    public final void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    final int getElementCapacity() {

        return elementsArray.length;
    }
}
