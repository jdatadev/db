package dev.jdata.db.utils.adt.lists;

interface IObjectDoublyLinkedListMutators<T> extends IListMutatorsMarker {

    void removeNode(Node<T> node);
}
