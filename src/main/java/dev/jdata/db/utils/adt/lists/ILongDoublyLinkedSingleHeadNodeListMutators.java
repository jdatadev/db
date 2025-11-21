package dev.jdata.db.utils.adt.lists;

interface ILongDoublyLinkedSingleHeadNodeListMutators extends INodeListMutatorsMarker {

    long removeNodeAndReturnValue(long toRemove);
}
