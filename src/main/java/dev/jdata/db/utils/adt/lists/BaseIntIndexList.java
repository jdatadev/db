package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IIntByIndexView;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntIndexList extends BaseIntegerIndexList<int[]> implements IIntIndexListCommon {

    BaseIntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, int[]::new, new int[initialCapacity], 0);
    }

    BaseIntIndexList(AllocationType allocationType, int value, boolean disambiguate) {
        super(allocationType, int[]::new, new int[] { value }, 1);
    }

    BaseIntIndexList(AllocationType allocationType, int[] values) {
        super(allocationType, int[]::new, values, values.length);
    }

    BaseIntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, int[]::new, values, numElements);

        Checks.checkNumElements(values, numElements);
    }

    BaseIntIndexList(AllocationType allocationType, IIntByIndexView toCopy, int numElements) {
        super(allocationType, int[]::new, toCopy.toArray(numElements, null, (p, c) -> new int[c]), numElements);
    }

    BaseIntIndexList(AllocationType allocationType, BaseIntIndexList toCopy) {
        super(allocationType, int[]::new, Arrays.copyOf(toCopy.getElementsArray(), toCopy.getIntNumElements()), toCopy.getIntNumElements());
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int num = getIntNumElements();
        final int[] elementsArray = getElementsArray();

        for (int i = 0; i < num; ++ i) {

            forEach.each(elementsArray[i], parameter);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int num = getIntNumElements();
        final int[] elementsArray = getElementsArray();

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
    public final int get(long index) {

        Checks.isIntIndex(index);

        return getElementsArray()[intIndex(index)];
    }

    @Override
    protected final int[] copyValues(int[] elements, long startIndex, long numElements) {

        checkIntCopyValuesParameters(elements, elements.length, startIndex, numElements);

        return Arrays.copyOfRange(elements, intIndex(startIndex), intNumElements(numElements));
    }

    @Override
    protected final void initializeWithValues(int[] values, long numElements) {

        checkIntIntitializeWithValuesParameters(values, values.length, numElements);

        initializeArrayList(values, intNumElements(numElements));
    }

    @Override
    final int getElementsCapacity() {

        return getElementsArray().length;
    }
}
