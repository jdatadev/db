package dev.jdata.db.utils.adt.lists;

interface ITailListRemovalMutators extends IListMutatorsMarker {

    void removeTail();

    default void removeTail(long numElements) {

        for (long i = 0L; i < numElements; ++ i) {

            removeTail();
        }
    }
}
