package dev.jdata.db.utils.adt.lists;

interface ILongSingleHeadNodeListMutators extends INodeListMutatorsMarker {

    long addHeadAndReturnNode(long value);

    long addTailAndReturnNode(long value);
}
