package dev.jdata.db.utils.adt.lists;

interface ILongDoublyLinkedSingleHeadNodeListMutable

        extends IDoublyLinkedSingleHeadNodeListMutable, ILongSingleHeadNodeListMutable, ILongTailListRemovalMutators, ILongDoublyLinkedSingleHeadNodeListMutators {

    @Override
    default void removeTail() {

        removeTailAndReturnValue();
    }

    @Override
    default void removeNode(long toRemove) {

        removeNodeAndReturnValue(toRemove);
    }
}
