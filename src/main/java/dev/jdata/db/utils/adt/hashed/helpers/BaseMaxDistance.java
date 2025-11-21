package dev.jdata.db.utils.adt.hashed.helpers;

abstract class BaseMaxDistance {

    interface BaseMaxDistanceHashedOperations<T, U, M> {

        long getCapacity(T hashed);

        M getMaxDistances(T hashed);

        void incrementNumElements(T hashed);
        void increaseCapacityAndRehash(T hashed);

        U getHashArray(T hashed);
    }
}
