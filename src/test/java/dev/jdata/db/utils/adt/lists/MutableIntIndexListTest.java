package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableIntIndexListTest extends BaseIntegerIndexListTest<IMutableIntIndexList> {

    @Override
    IMutableIntIndexList createArray(int initialCapacity) {

        return HeapMutableIntIndexList.create(AllocationType.HEAP, initialCapacity);
    }

    @Override
    <P> void forEach(IMutableIntIndexList list, P parameter, IIntForEach<P, RuntimeException> forEach) {

        list.forEach(parameter, (e, p) -> forEach.each(Integers.checkLongToInt(e), p));
    }

    @Override
    <P1, P2, R> R forEachWithResult(IMutableIntIndexList list, R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, RuntimeException> forEach) {

        return list.forEachWithResult(defaultResult, parameter1, parameter2, (e, p1, p2) -> forEach.each(Integers.checkLongToInt(e), p1, p2));
    }

    @Override
    int get(IMutableIntIndexList list, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(list.get(index));
    }

    @Override
    void addTail(IMutableIntIndexList list, int value) {

        list.addTail(value);
    }

    @Override
    int[] toArray(IMutableIntIndexList list) {

        return Array.mapToInt(list, Integers.checkUnsignedLongToUnsignedInt(list.getNumElements()), null, (l, i, p) -> Integers.checkLongToInt(l.get(i)));
    }
}
