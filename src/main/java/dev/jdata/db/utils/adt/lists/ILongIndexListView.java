package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import dev.jdata.db.utils.adt.elements.ILongByIndexOrderedOnlyElementsView;

public interface ILongIndexListView extends ILongListView, ILongByIndexOrderedOnlyElementsView {

    @Override
    default long[] toArray() {

        return ILongByIndexOrderedOnlyElementsView.super.toArray();
    }

    @Override
    default long getHead() {

        if (isEmpty()) {

            throw new NoSuchElementException();
        }

        return get(0L);
    }

    @Override
    default long getTail() {

        if (isEmpty()) {

            throw new NoSuchElementException();
        }

        return get(getNumElements() - 1L);
    }
}
