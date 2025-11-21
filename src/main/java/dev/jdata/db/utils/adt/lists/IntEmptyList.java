package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IntEmptyOrderedOnlyElements;

abstract class IntEmptyList extends IntEmptyOrderedOnlyElements implements IIntListView {

    @Override
    public final int getHead() {

        throw ElementsExceptions.emptyException();
    }

    @Override
    public final int getTail() {

        throw ElementsExceptions.emptyException();
    }
}
