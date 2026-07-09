package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

abstract class ObjectSinglyLinkedNode<T, N extends ObjectSinglyLinkedNode<T, N, L>, L extends BaseObjectLinkedList<T, N, L>> extends SinglyLinkedNode<N, L> {

    T element;

    public final T getElement() {
        return element;
    }

    public final void setElement(T element) {

        Objects.requireNonNull(element);
        Checks.areNotSame(this.element, element);

        this.element = element;
    }
}
