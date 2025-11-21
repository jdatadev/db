package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

public abstract class InheritableMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T, VALUES extends InheritableObjectInnerOuterNodeListValues<T>>

        extends BaseMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T, VALUES> {

    protected InheritableMutableObjectLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            IntFunction<VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory::apply);
    }
}
