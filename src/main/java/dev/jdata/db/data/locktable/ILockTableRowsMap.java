package dev.jdata.db.data.locktable;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

interface ILockTableRowsMap extends IOnlyElementsView {

    public static final long NO_LOCK_INDEX = -1L;

    long getLockIndex(long key);

    long getLock(long index);
    long getLockInfoListsHeadNode(long index);
    long getLockInfoListsTailNode(long index);

    void put(long key, long lock, long lockInfoListsHeadNode, long lockInfoListsTailNode);

    void remove(long key);

    LockedRows getLockedRows();
    long getLockHoldersHeadNode(long key);
}
