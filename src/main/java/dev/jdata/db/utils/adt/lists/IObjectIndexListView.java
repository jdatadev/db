package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectByIndexOrderedElementsView;

public interface IObjectIndexListView<T> extends IObjectListView<T>, IObjectByIndexOrderedElementsView<T> {

    @Override
    default T getHead() {

        return get(0L);
    }

    @Override
    default T getTail() {

        return get(getNumElements() - 1L);
    }
}

