package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.marker.IAddable;

interface IUnorderedAddable<T extends IOnlyElementsView & IElementsIterable> extends IAddable {

    void addUnordered(T elements);
}
