package dev.jdata.db.utils.adt.lists;

public interface IIntList {

    boolean contains(int value);
    boolean containsOnly(int value);

    @FunctionalInterface
    public interface ContainsOnlyPredicate {

        boolean test(int inputValue, int listValue);
    }

    boolean containsOnly(int value, ContainsOnlyPredicate predicate);

    int[] toArray();
}
