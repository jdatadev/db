package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiPredicate;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class BaseObjectDoublyLinkedList<T> extends BaseObjectLinkedList<T, Node<T>, BaseObjectDoublyLinkedList<T>> implements IObjectDoublyLinkedListCommon<T> {

    BaseObjectDoublyLinkedList(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final Node<T> getHeadNode() {

        return getListHeadNode();
    }

    @Override
    public final Node<T> getTailNode() {

        return getListTailNode();
    }

    @Override
    public final Node<T> getNext(Node<T> node) {

        Objects.requireNonNull(node);

        return node.next;
    }

    @Override
    public final Node<T> getPrevious(Node<T> node) {

        Objects.requireNonNull(node);

        return node.previous;
    }

    @Override
    public <P> boolean contains(Node<T> startNode, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(startNode);
        checkNode(startNode);
        Objects.requireNonNull(predicate);

        boolean found = false;

        for (Node<T> node = startNode; node != null; node = node.next) {

            if (predicate.test(node.element, parameter)) {

                if (found) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                found = true;
            }
        }

        return found;
    }

    @Override
    public final <P> Node<T> findAtMostOneNode(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        Node<T> found = null;

        for (Node<T> node = getListHeadNode(); node != null; node = node.next) {

            if (predicate.test(node.element, parameter)) {

                if (found != null) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                found = node;
            }
        }

        return found;
    }
}
