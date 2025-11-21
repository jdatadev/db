package dev.jdata.db.utils.adt.elements;

interface IMutableElementsAllocator<T extends IMutableElements> extends IBaseElementsAllocator {

    T createMutable(long minimumCapacity);

    void freeMutable(T immutable);
}
