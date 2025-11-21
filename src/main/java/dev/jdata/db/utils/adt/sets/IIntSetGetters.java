package dev.jdata.db.utils.adt.sets;

interface IIntSetGetters extends ISetGettersMarker {

    IHeapIntSet toHeapAllocated();
}
