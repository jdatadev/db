package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.marker.IMutators;

interface IIntArrayMutators extends IMutators {

    void add(int value);

    void set(long index, int value);
}
