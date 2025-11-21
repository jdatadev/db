package dev.jdata.db.utils.adt.lists;

interface ILongHeadListMutators extends IHeadListMutators {

    void addHead(long value);

    long removeHeadAndReturnValue();
}
