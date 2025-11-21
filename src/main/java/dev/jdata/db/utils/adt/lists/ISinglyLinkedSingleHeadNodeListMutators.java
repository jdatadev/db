package dev.jdata.db.utils.adt.lists;

interface ISinglyLinkedSingleHeadNodeListMutators extends INodeListMutatorsMarker {

    long removeNodeByFindingPrevious(long toRemove);
}
