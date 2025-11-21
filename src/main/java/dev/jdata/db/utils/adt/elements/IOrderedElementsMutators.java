package dev.jdata.db.utils.adt.elements;

public interface IOrderedElementsMutators<T> extends IElementsMutatorsMarker {

    void sort(T comparator);
}
