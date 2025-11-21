package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ICopyToImmutable;
import dev.jdata.db.utils.adt.elements.IIntElementsRandomAccessRemovalMutable;
import dev.jdata.db.utils.adt.elements.IMutableIntUnorderedOnlyElements;

interface IBaseMutableIntSet<T extends IBaseIntSet, U extends IBaseIntSetAllocator<T>>

        extends IMutableIntUnorderedOnlyElements, IBaseMutableSet, IIntSetCommon, IIntElementsRandomAccessRemovalMutable, IIntSetMutators, ICopyToImmutable<T, U> {

    @Override
    default void addUnordered(int value) {

        addToSet(value);
    }
}
