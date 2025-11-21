package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.ILongElementsRandomAccessRemovalMutable;
import dev.jdata.db.utils.adt.elements.IMutableLongUnorderedOnlyElements;

public interface IBaseMutableLongSet

        extends IMutableLongUnorderedOnlyElements, IMutableSetType, IBaseLongSetCommon, ILongElementsRandomAccessRemovalMutable, ILongSetMutators, ILongAnyOrderAddable {

    @Override
    default void addUnordered(long value) {

        addToSet(value);
    }

    @Override
    default void addInAnyOrder(long value) {

        addToSet(value);
    }
}
