package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongByIndexOrderedOnlyElementsView;

public interface ILongIndexListView extends ILongListView, ILongByIndexOrderedOnlyElementsView {

    @Override
    default long getHead() {

        return get(0L);
    }

    @Override
    default long getTail() {

        return get(getNumElements() - 1L);
    }
}
