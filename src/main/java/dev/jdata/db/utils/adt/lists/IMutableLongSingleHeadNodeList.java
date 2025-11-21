package dev.jdata.db.utils.adt.lists;

public interface IMutableLongSingleHeadNodeList extends IMutableSingleHeadNodeList, ILongSingleHeadNodeListCommon, ILongSingleHeadNodeListMutable  {

    @Override
    default long removeHeadAndReturnValue() {

        final long result = getValue(getHeadNode());

        removeHead();

        return result;
    }
}
