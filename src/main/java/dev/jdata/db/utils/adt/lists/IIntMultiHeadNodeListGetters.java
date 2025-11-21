package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IIntElementPredicate;

interface IIntMultiHeadNodeListGetters extends IMultiHeadNodeListGettersMarker {

    boolean contains(int value, long headNode);
    boolean containsOnly(int value, long headNode);
    boolean containsOnly(int value, long headNode, IIntContainsOnlyPredicate predicate);
    <P> boolean contains(long headNode, P parameter, IIntElementPredicate<P> predicate);

    long findAtMostOneNode(int value, long headNode);
    <P> int findAtMostOneValue(int defaultValue, long headNode, P parameter, IIntElementPredicate<P> predicate);

    int[] toArray(long headNode);
}
