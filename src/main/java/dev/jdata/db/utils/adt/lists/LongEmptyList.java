package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import dev.jdata.db.utils.adt.elements.LongEmptyOrderedOnlyElements;

abstract class LongEmptyList extends LongEmptyOrderedOnlyElements implements ILongListView {

    @Override
    public final long getHead() {

        throw new NoSuchElementException();
    }

    @Override
    public final long getTail() {

        throw new NoSuchElementException();
    }
}
