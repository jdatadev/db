package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongIndexListTest extends BaseIntegerIndexListTest<IBaseMutableLongIndexList> {

    @Override
    IMutableLongIndexList createArray(int initialCapacity) {

        return new HeapMutableLongIndexList(AllocationType.HEAP, initialCapacity);
    }

    @Override
    <P> void forEach(IBaseMutableLongIndexList list, P parameter, IIntForEach<P, RuntimeException> forEach) {

        list.forEach(parameter, (e, p) -> forEach.each(Integers.checkLongToInt(e), p));
    }

    @Override
    <P1, P2, R> R forEachWithResult(IBaseMutableLongIndexList list, R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, RuntimeException> forEach) {

        return list.forEachWithResult(defaultResult, parameter1, parameter2, (e, p1, p2) -> forEach.each(Integers.checkLongToInt(e), p1, p2));
    }

    @Override
    int get(IBaseMutableLongIndexList list, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(list.get(index));
    }

    @Override
    void addTail(IBaseMutableLongIndexList list, int value) {

        list.addTail(value);
    }

    @Override
    boolean removeAtMostOne(IBaseMutableLongIndexList list, int value) {

        return list.removeAtMostOne(value);
    }

    @Override
    int[] toArray(IBaseMutableLongIndexList list) {

        return Array.mapToInt(list, Integers.checkUnsignedLongToUnsignedInt(list.getNumElements()), null, (l, i, p) -> Integers.checkLongToInt(l.get(i)));
    }
}
