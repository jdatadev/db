package dev.jdata.db.utils.adt.lists;

public interface IMutableIntLargeSinglyLinkedMultiHeadNodeList<T> extends IMutableIntSinglyLinkedMultiHeadNodeList<T> {

    public static <T> IMutableIntLargeSinglyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return new MutableIntLargeSinglyLinkedMultiHeadNodeList<>(initialOuterCapacity, innerCapacity);
    }
/*
    public static <T> IMutableIntLargeSinglyLinkedMultiHeadNodeList<T> from(IIntOrderedElementsView elements) {

        if (elements instanceof mutableint) {

            return new MutableIntLargeSinglyLinkedMultiHeadNodeList<>(initialOuterCapacity, innerCapacity);
        }
    }
*/
}
