package dev.jdata.db.utils.adt.lists;

public interface IMutableLongSingleHeadNodeList extends IMutableSingleHeadNodeList, ILongSingleHeadNodeListCommon, ILongSingleHeadNodeListMutators  {

    @Override
    default long removeHeadAndReturnValue() {

        final long result = getValue(getHeadNode());

        removeHead();

        return result;
    }
}
