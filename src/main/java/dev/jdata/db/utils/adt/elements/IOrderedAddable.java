package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.marker.IAddable;

public interface IOrderedAddable<T extends IElementsIterable> extends IAddable {

    void addTail(T elementsIterable);
}
