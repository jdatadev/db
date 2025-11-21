package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongElementsRandomAccessRemovalMutable;
import dev.jdata.db.utils.adt.elements.IMutableLongUnorderedOnlyElements;

interface IBaseMutableLongSet<T extends IBaseLongSet, U extends IBaseLongSetAllocator<T>>

        extends IMutableLongUnorderedOnlyElements, IBaseMutableSet, ILongSetCommon, ILongElementsRandomAccessRemovalMutable, ILongSetMutators {

    T toImmutable(U longSetAllocator);

    @Override
    default void addUnordered(long value) {

        addToSet(value);
    }
}
