package dev.jdata.db.utils.adt.lists;

interface IHeadListMutators extends IListMutatorsMarker {

    void removeHead();

    default void removeHead(long numElements) {

        for (long i = 0L; i < numElements; ++ i) {

            removeHead();
        }
    }
}
