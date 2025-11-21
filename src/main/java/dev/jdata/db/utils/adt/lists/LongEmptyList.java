package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.LongEmptyOrderedOnlyElements;

abstract class LongEmptyList extends LongEmptyOrderedOnlyElements implements ILongListView {

    @Override
    public final long getHead() {

        throw ElementsExceptions.emptyException();
    }

    @Override
    public final long getTail() {

        throw ElementsExceptions.emptyException();
    }
}
