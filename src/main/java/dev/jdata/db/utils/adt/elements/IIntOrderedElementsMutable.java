package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.function.IntComparator;

interface IIntOrderedElementsMutable extends IOrderedElementsMutable<IntComparator>, IIntOrderedElementsMutators, IIntOrderedAddTailElementsMutators, IIntAnyOrderAddable {

    @Override
    default void addInAnyOrder(int value) {

        addTail(value);
    }
}
