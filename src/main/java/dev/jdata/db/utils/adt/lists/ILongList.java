package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements;

public interface ILongList extends ILongElements {

    boolean containsOnly(long value);

    @FunctionalInterface
    public interface ContainsOnlyPredicate {

        boolean test(long inputValue, long listValue);
    }

    boolean containsOnly(long value, ContainsOnlyPredicate predicate);

    long[] toArray();
}
