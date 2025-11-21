package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.marker.IMutators;

interface IByteArrayMutators extends IMutators {

    void add(byte value);

    void set(long index, byte value);
}
