package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntByIndexOrderedOnlyElementsView;

public interface IIntIndexListView extends IIntListView, IIntByIndexOrderedOnlyElementsView {

    @Override
    default int getHead() {

        return get(0L);
    }

    @Override
    default int getTail() {

        return get(getNumElements() - 1L);
    }
}
