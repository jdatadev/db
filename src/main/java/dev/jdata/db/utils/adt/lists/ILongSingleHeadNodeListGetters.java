package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElementPredicate;

interface ILongSingleHeadNodeListGetters extends ISingleHeadNodeListGettersMarker {

    long getValue(long node);

    long findAtMostOneNode(long value);

    <P> long findAtMostOneValue(long defaultValue, P parameter, ILongElementPredicate<P> predicate);
}
