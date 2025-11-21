package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

public abstract class InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES extends InheritableLongInnerOuterNodeListValues>

        extends BaseMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES> {

    protected InheritableMutableLongLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            IntFunction<VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory::apply);
    }
}
