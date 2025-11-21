package dev.jdata.db.utils.adt.lists;

interface IIntDoublyLinkedSingleHeadNodeListMutators extends INodeListMutatorsMarker {

    int removeNodeAndReturnValue(long toRemove);
}
