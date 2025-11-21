package dev.jdata.db.utils.adt.elements;

import java.util.Comparator;

interface IOrderedElementsMutators<T> extends IElementsMutatorsMarker {

    void sort(Comparator<? super T> comparator);
}
