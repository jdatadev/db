package dev.jdata.db.utils.adt.lists;

interface IIntHeadListMutators extends IHeadListMutators {

    void addHead(int value);

    int removeHeadAndReturnValue();
}
