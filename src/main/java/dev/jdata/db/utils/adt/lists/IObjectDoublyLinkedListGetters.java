package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;

interface IObjectDoublyLinkedListGetters<T> extends IObjectListGetters<T> {

    Node<T> getHeadNode();
    Node<T> getTailNode();

    Node<T> getNext(Node<T> node);
    Node<T> getPrevious(Node<T> node);

    <P> boolean contains(Node<T> startNode, P parameter, BiPredicate<T, P> predicate);

    <P> Node<T> findAtMostOneNode(P parameter, BiPredicate<T, P> predicate);
}
