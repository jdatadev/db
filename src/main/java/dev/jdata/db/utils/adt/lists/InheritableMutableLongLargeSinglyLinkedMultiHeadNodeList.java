package dev.jdata.db.utils.adt.lists;

public abstract class InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES extends InheritableLongInnerOuterNodeListValues>

        extends BaseMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES> {

    protected InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }
}
