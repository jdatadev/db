package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntIndexList extends BaseIntegerIndexList<int[]> implements IIntIndexListCommon {

    BaseIntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType);

        Checks.isInitialCapacity(initialCapacity);

        this.elementsArray = new int[initialCapacity];
        this.numElements = 0;
    }

    BaseIntIndexList(AllocationType allocationType, int value, boolean disambiguate) {
        super(allocationType, new int[] { value }, 1);
    }

    BaseIntIndexList(AllocationType allocationType, int[] values) {
        super(allocationType, values, values.length);
    }

    BaseIntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, values, numElements);

        Checks.checkNumElements(values, numElements);
    }

    BaseIntIndexList(BaseIntIndexList toCopy) {
        super(AllocationType.HEAP);

        final int toCopyNumElements = toCopy.numElements;

        this.elementsArray = Arrays.copyOf(toCopy.elementsArray, toCopyNumElements);
        this.numElements = toCopyNumElements;
    }

    final void initialize(int[] values, int numElements) {

        initialize(values, numElements, values.length);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            forEach.each(elementsArray[i], parameter);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

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
    public final int get(long index) {

        Checks.isIndex(index);

        return elementsArray[IByIndexView.intIndex(index)];
    }

    final int getElementCapacity() {

        return elementsArray.length;
    }
}
