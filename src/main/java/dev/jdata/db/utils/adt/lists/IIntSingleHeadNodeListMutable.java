package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface IIntSingleHeadNodeListMutable extends IMutableMarker, IIntSingleHeadNodeListMutators {

    @Override
    default void addHead(int value) {

        addHeadAndReturnNode(value);
    }

    @Override
    default void addTail(int value) {

        addTailAndReturnNode(value);
    }

    @Override
    default void removeHead() {

        removeHeadAndReturnValue();
    }
}
