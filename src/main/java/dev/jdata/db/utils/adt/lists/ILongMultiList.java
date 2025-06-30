package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements.IContainsOnlyPredicate;

public interface ILongMultiList {

    public boolean containsOnly(long value, long headNode, IContainsOnlyPredicate predicate);

    long findNode(long value, long headNode);

    long[] toArray(long headNode);
}
