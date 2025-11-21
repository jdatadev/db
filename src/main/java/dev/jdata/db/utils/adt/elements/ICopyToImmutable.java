package dev.jdata.db.utils.adt.elements;

public interface ICopyToImmutable<T extends IElements, U extends IElementsAllocator<T, ?, ?, ?>> {

    T copyToImmutable(U immutableAllocator);
}
