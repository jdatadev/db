package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IObjectElementPredicate;

interface IObjectMultiHeadNodeListGetters<T> extends IMultiHeadNodeListGettersMarker {

    boolean contains(T value, long headNode);
    boolean containsOnly(T value, long headNode);
    boolean containsOnly(T value, long headNode, IObjectContainsOnlyPredicate<T> predicate);
    <P> boolean contains(long headNode, P parameter, IObjectElementPredicate<T, P> predicate);

    long findAtMostOneNode(T value, long headNode);
    <P> T findAtMostOneValue(T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate);

    T[] toArray(long headNode);
}
