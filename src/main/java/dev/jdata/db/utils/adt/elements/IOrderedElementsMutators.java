package dev.jdata.db.utils.adt.elements;

interface IOrderedElementsMutators<T> extends IElementsMutatorsMarker {

    void sort(T comparator);

    // void reverse();
}
