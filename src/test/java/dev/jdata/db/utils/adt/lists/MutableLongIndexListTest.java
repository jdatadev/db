package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEach;
import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEachWithResult;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongIndexListTest extends BaseIntegerIndexListTest<IMutableLongIndexList> {

    @Override
    IMutableLongIndexList createArray(int initialCapacity) {

        return new MutableLongIndexList(initialCapacity);
    }

    @Override
    <P> void forEach(IMutableLongIndexList list, P parameter, IForEach<P, RuntimeException> forEach) {

        list.forEach(parameter, (e, p) -> forEach.each(Integers.checkLongToInt(e), p));
    }

    @Override
    <P1, P2, R> R forEachWithResult(IMutableLongIndexList list, R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, RuntimeException> forEach) {

        return list.forEachWithResult(defaultResult, parameter1, parameter2, (e, p1, p2) -> forEach.each(Integers.checkLongToInt(e), p1, p2));
    }

    @Override
    int get(IMutableLongIndexList list, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(list.get(index));
    }

    @Override
    void addTail(IMutableLongIndexList list, int value) {

        list.addTail(value);
    }

    @Override
    boolean removeAtMostOne(IMutableLongIndexList list, int value) {

        return list.removeAtMostOne(value);
    }

    @Override
    int[] toArray(IMutableLongIndexList list) {

        return Array.mapToInt(list, Integers.checkUnsignedLongToUnsignedInt(list.getNumElements()), null, (l, i, p) -> Integers.checkLongToInt(l.get(i)));
    }
}
