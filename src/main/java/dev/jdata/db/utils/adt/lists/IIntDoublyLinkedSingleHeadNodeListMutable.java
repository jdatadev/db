package dev.jdata.db.utils.adt.lists;

interface IIntDoublyLinkedSingleHeadNodeListMutable

        extends IDoublyLinkedSingleHeadNodeListMutable, IIntSingleHeadNodeListMutable, IIntTailListRemovalMutators, IIntDoublyLinkedSingleHeadNodeListMutators {

    @Override
    default void removeNode(long toRemove) {

        removeNodeAndReturnValue(toRemove);
    }
}
