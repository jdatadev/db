package dev.jdata.db.utils.adt.sets;

interface ILongSetGetters extends ISetGettersMarker {

    IHeapLongSet toHeapAllocated();
}
