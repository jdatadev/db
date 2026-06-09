package dev.jdata.db.utils.adt.lists;

public final class Node<T> extends ObjectSinglyLinkedNode<T, Node<T>, BaseObjectDoublyLinkedList<T>> {

    Node<T> previous;

    public Node<T> getPrevious() {

        checkNodeAllocated();

        return previous;
    }
}
