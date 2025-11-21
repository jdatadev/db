package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList;

abstract class BaseLongToObjectBucketMapMultiHeadSinglyLinkedNodeList<

                INSTANCE,
                T,
                LIST extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedNodeList<INSTANCE, T, LIST, VALUES>,
                VALUES extends BaseLongToObjectValues<T>>

        extends InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES> {

    BaseLongToObjectBucketMapMultiHeadSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity, IntFunction<VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final T getObjectValue(long node) {

        return getValues().getObjectValue(getOuterIndex(node), getInnerIndex(node));
    }

    public final void setObjectValue(long node, T value) {

        getValues().setObjectValue(getOuterIndex(node), getInnerIndex(node), value);
    }

    public final T getAndSetObjectValue(long node, T value) {

        return getValues().getAndSetObjectValue(getOuterIndex(node), getInnerIndex(node), value);
    }
}