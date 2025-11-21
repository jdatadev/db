package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IIntElementsRandomAccessRemovalMutable;
import dev.jdata.db.utils.adt.elements.IMutableIntUnorderedOnlyElements;

interface IBaseMutableIntSet

        extends IMutableIntUnorderedOnlyElements, IMutableSetType, IBaseIntSetCommon, IIntElementsRandomAccessRemovalMutable, IIntSetMutators, IIntAnyOrderAddable {

    @Override
    default void addUnordered(int value) {

        addToSet(value);
    }

    @Override
    default void addInAnyOrder(int value) {

        addToSet(value);
    }
}
