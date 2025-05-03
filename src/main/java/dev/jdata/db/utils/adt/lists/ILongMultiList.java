package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.ILongList.ContainsOnlyPredicate;

public interface ILongMultiList {

    public boolean containsOnly(long value, long headNode, ContainsOnlyPredicate predicate);

    long findNode(long value, long headNode);

    long[] toArray(long headNode);
}
