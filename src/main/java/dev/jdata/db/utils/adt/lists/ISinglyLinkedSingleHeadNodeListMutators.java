package dev.jdata.db.utils.adt.lists;

interface ISinglyLinkedSingleHeadNodeListMutators extends INodeListMutatorsMarker {

    void removeNodeByFindingPrevious(long toRemove);
}
