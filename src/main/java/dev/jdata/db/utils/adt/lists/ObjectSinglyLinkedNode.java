package dev.jdata.db.utils.adt.lists;

abstract class ObjectSinglyLinkedNode<T, N extends ObjectSinglyLinkedNode<T, N, L>, L extends BaseObjectLinkedList<T, N, L>> extends SinglyLinkedNode<N, L> {

    T element;

    public final T getElement() {
        return element;
    }
}
