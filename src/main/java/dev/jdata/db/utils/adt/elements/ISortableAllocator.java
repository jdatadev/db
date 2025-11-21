package dev.jdata.db.utils.adt.elements;

public interface ISortableAllocator<T extends IElementsIterable, U, V extends IElements> {

    V sortedOf(T elements, U comparator);
}
