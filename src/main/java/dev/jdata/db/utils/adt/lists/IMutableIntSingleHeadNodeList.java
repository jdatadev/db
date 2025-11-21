package dev.jdata.db.utils.adt.lists;

interface IMutableIntSingleHeadNodeList extends IMutableSingleHeadNodeList, IIntSingleHeadNodeListCommon, IIntSingleHeadNodeListMutable  {

    @Override
    default int removeHeadAndReturnValue() {

        final int result = getValue(getHeadNode());

        removeHead();

        return result;
    }
}
