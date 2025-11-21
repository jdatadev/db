package dev.jdata.db.utils.adt.lists;

public abstract class InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES> {

    protected InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList(int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }
}
