package dev.jdata.db.utils.adt.lists;

public interface IMutableObjectMultiHeadNodeList<INSTANCE, T>

        extends IMutableMultiHeadNodeList<INSTANCE>, IObjectMultiHeadNodeListCommon<T>, IObjectNodeListCommon<T>, IObjectMultiHeadNodeListMutators<INSTANCE, T> {

}
