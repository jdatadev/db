package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.lists.BaseLargeLongMultiHeadSinglyLinkedList;

public abstract class BaseLongToObjectBucketMapMultiHeadSinglyLinkedList<

                INSTANCE,
                T,
                LIST extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T, LIST, VALUES>,
                VALUES extends BaseLongToObjectValues<INSTANCE, T, LIST, VALUES>>

        extends BaseLargeLongMultiHeadSinglyLinkedList<INSTANCE, LIST, VALUES> {

    protected BaseLongToObjectBucketMapMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, ILargeListValuesFactory<long[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
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