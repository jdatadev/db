package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;

public interface IMutableDoublyLinkedList<T> extends IMutableObjectLinkedList<T>, IObjectDoublyLinkedListCommon<T>, IDoublyLinkedListMutable<T> {

    @Override
    default <P> Node<T> removeAtMostOneNode(P parameter, BiPredicate<T, P> predicate) {

        final Node<T> node = findAtMostOneNode(parameter, predicate);

        if (node != null) {

            removeNode(node);
        }

        return node;
    }
}
