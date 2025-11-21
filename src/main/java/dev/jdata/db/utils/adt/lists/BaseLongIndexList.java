package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.ILongByIndexView;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongIndexList extends BaseIntegerIndexList<long[]> implements ILongIndexListCommon {

    BaseLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, long[]::new, new long[initialCapacity], 0);
    }

    BaseLongIndexList(AllocationType allocationType, long value) {
        super(allocationType, long[]::new, new long[] { value }, 1);
    }

    BaseLongIndexList(AllocationType allocationType, long[] values) {
        super(allocationType, long[]::new, values, values.length);
    }

    BaseLongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, long[]::new, values, numElements);

        Checks.checkNumElements(values, numElements);
    }

    BaseLongIndexList(AllocationType allocationType, ILongByIndexView toCopy, int numElements) {
        super(allocationType, long[]::new, toCopy.toArray(numElements, null, (p, c) -> new long[c]), numElements);
    }

    BaseLongIndexList(AllocationType allocationType, BaseLongIndexList toCopy) {
        super(allocationType, Arrays.copyOf(toCopy.getElementsArray(), toCopy.getIntNumElements()), toCopy.getIntNumElements());
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int num = getIntNumElements();
        final long[] elementsArray = getElementsArray();

        for (int i = 0; i < num; ++ i) {

            forEach.each(elementsArray[i], parameter);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int num = getIntNumElements();
        final long[] elementsArray = getElementsArray();

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

        Checks.isIntIndex(index);

        return getElementsArray()[intIndex(index)];
    }

    @Override
    protected final long[] copyValues(long[] elements, long startIndex, long numElements) {

        checkIntCopyValuesParameters(elements, elements.length, startIndex, numElements);

        return Arrays.copyOfRange(elements, intIndex(startIndex), intNumElements(numElements));
    }

    @Override
    protected final void initializeWithValues(long[] values, long numElements) {

        checkIntIntitializeWithValuesParameters(values, values.length, numElements);

        initializeArrayList(values, intNumElements(numElements));
    }

    @Override
    final int getElementsCapacity() {

        return getElementsArray().length;
    }
}
