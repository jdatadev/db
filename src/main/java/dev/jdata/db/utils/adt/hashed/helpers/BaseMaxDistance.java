package dev.jdata.db.utils.adt.hashed.helpers;

import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseMaxDistance extends PrintDebug {

    interface BaseMaxDistanceHashedOperations<T, U, M> {

        long getCapacity(T hashed);

        M getMaxDistances(T hashed);

        void incrementNumElements(T hashed);
        void increaseCapacityAndRehash(T hashed);

        U getHashArray(T hashed);
    }
}
