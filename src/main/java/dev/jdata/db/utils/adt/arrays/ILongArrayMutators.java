package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.marker.IMutators;

interface ILongArrayMutators extends IMutators {

    void add(long value);

    void set(long index, long value);
}
