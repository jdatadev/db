package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;

interface ILongMultiHeadNodeListGetters extends IMultiHeadNodeListGettersMarker {

    boolean contains(long value, long headNode);
    boolean containsOnly(long value, long headNode);
    boolean containsOnly(long value, long headNode, ILongContainsOnlyPredicate predicate);
    <P> boolean contains(long headNode, P parameter, ILongElementPredicate<P> predicate);

    long findAtMostOneNode(long value, long headNode);
    <P> long findAtMostOneValue(long defaultValue, long headNode, P parameter, ILongElementPredicate<P> predicate);

    long[] toArray(long headNode);
}
