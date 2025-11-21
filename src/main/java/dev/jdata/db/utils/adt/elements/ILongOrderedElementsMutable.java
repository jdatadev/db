package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.function.LongComparator;

interface ILongOrderedElementsMutable extends IOrderedElementsMutable<LongComparator>, ILongOrderedElementsMutators, ILongOrderedAddTailElementsMutators, ILongAnyOrderAddable {

    @Override
    default void addInAnyOrder(long value) {

        addTail(value);
    }
}
