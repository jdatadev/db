package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;

interface IObjectDoublyLinkedListMutators<T> extends IListMutatorsMarker {

    void removeNode(Node<T> node);

    <P> Node<T> removeAtMostOneNode(P parameter, BiPredicate<T, P> predicate);

    default <P> Node<T> removeExactlyOneNode(P parameter, BiPredicate<T, P> predicate) {

        final Node<T> result = removeAtMostOneNode(parameter, predicate);

        if (result == null) {

            throw ElementsExceptions.lessThanOneFoundException();
        }

        return result;
    }
}
