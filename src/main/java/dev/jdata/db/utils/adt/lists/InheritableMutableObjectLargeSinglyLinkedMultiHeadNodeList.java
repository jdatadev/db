package dev.jdata.db.utils.adt.lists;

public abstract class InheritableMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T, VALUES extends InheritableObjectInnerOuterNodeListValues<T>>

        extends BaseMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T, VALUES> {

    protected InheritableMutableObjectLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<T[], IObjectInnerOuterNodeListInternal<T>, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }
}
