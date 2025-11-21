package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IIntByIndexOrderedOnlyElementsView;

public interface IIntIndexListView extends IIntListView, IIntByIndexOrderedOnlyElementsView {

    @Override
    default int[] toArray() {

        return IIntByIndexOrderedOnlyElementsView.super.toArray();
    }

    @Override
    default int getHead() {

        if (isEmpty()) {

            throw ElementsExceptions.emptyException();
        }

        return get(0L);
    }

    @Override
    default int getTail() {

        if (isEmpty()) {

            throw ElementsExceptions.emptyException();
        }

        return get(getNumElements() - 1L);
    }
}
