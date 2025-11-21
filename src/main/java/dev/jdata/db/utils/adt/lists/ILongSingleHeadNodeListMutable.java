package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElementsRandomAccessRemovalMutable;
import dev.jdata.db.utils.adt.elements.ILongOrderedAddTailElementsMutators;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface ILongSingleHeadNodeListMutable

        extends IMutableMarker, ILongElementsRandomAccessRemovalMutable, ILongHeadListMutators, ILongOrderedAddTailElementsMutators, ILongSingleHeadNodeListMutators {

    @Override
    default void addHead(long value) {

        addHeadAndReturnNode(value);
    }

    @Override
    default void addTail(long value) {

        addTailAndReturnNode(value);
    }

    @Override
    default void removeHead() {

        removeHeadAndReturnValue();
    }
}
