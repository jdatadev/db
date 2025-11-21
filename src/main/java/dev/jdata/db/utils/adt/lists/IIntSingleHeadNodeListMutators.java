package dev.jdata.db.utils.adt.lists;

interface IIntSingleHeadNodeListMutators extends INodeListMutatorsMarker {

    long addHeadAndReturnNode(int value);

    long addTailAndReturnNode(int value);
}
