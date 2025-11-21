package dev.jdata.db.utils.adt.elements;

public interface ISortableAllocator<T extends IElementsIterable & IOnlyElementsView, U, V extends IElements & IOnlyElementsView> {

    V sortedOf(T elements, U comparator);
}
