package dev.jdata.db.data.locktable;

import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.adt.lists.IntMultiList;
import dev.jdata.db.utils.adt.lists.LongMultiList;

interface ILockTableRowsMap extends Elements {

    public static final long NO_LOCK_INDEX = -1L;

    long getLockIndex(long key);

    long getLock(long index);
    long getTransactionIdListsHeadNode(long index);
    long getTransactionIdListsTailNode(long index);
    long getStatementIdListsHeadNode(long index);
    long getStatementIdListsTailNode(long index);

    void put(long key, long lock, long transactionIdListsHeadNode, long transactionIdListsTailNode, long statementIdListsHeadNode, long statementIdListsTailNode);

    boolean remove(long key);

    LockedRows getLockedRows();
    LockHolders getLockHolders(long key, LongMultiList transactionIdLists, IntMultiList statementIdLists);
}
