package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import dev.jdata.db.utils.adt.elements.IntEmptyOrderedOnlyElements;

abstract class IntEmptyList extends IntEmptyOrderedOnlyElements implements IIntListView {

    @Override
    public final int getHead() {

        throw new NoSuchElementException();
    }

    @Override
    public final int getTail() {

        throw new NoSuchElementException();
    }
}
