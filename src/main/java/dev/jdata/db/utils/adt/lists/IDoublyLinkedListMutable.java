package dev.jdata.db.utils.adt.lists;

public interface IDoublyLinkedListMutable<T> extends IObjectLinkedListMutable<T>, IObjectDoublyLinkedListMutators<T>, ITailListRemovalMutators {

    @Override
    default void removeTail() {

        removeTailAndReturnValue();
    }
}
