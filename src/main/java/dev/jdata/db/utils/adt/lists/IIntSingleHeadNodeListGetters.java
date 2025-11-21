package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntElementPredicate;

interface IIntSingleHeadNodeListGetters extends ISingleHeadNodeListGettersMarker {

    long findAtMostOneNode(int value);

    <P> int findAtMostOneValue(int defaultValue, P parameter, IIntElementPredicate<P> predicate);
}
